package tsygvintsev.watering_diary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tsygvintsev.watering_diary.entity.WateringRecord;
import tsygvintsev.watering_diary.repository.WateringRecordRepository;
import tsygvintsev.watering_diary.repository.UserPlantRepository;
import tsygvintsev.watering_diary.entity.UserPlant;
import tsygvintsev.watering_diary.entity.PlantType;
import tsygvintsev.watering_diary.entity.Material;
import tsygvintsev.watering_diary.entity.Conditions;
import tsygvintsev.watering_diary.repository.PlantTypeRepository;
import tsygvintsev.watering_diary.repository.MaterialRepository;
import tsygvintsev.watering_diary.repository.ConditionsRepository;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.List;

/**
 * Сервис для управления записями полива.
 * Содержит бизнес-логику расчёта рекомендаций и учёта погрешностей полива.
 */
@Service
public class WateringRecordService {

    @Autowired
    private WateringRecordRepository wateringRecordRepository;

    @Autowired
    private UserPlantRepository userPlantRepository;

    @Autowired
    private PlantTypeRepository plantTypeRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private ConditionsRepository conditionsRepository;

    /**
     * Получить все записи полива.
     *
     * @return список всех записей полива
     */
    public List<WateringRecord> getAllWateringRecords() {
        return wateringRecordRepository.findAll();
    }

    /**
     * Получить запись полива по ID.
     *
     * @param id уникальный идентификатор записи
     * @return найденная запись
     * @throws ResponseStatusException если запись не найдена
     */
    public WateringRecord getWateringRecordById(Integer id) {
        return wateringRecordRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не существует записи о поливе с таким id."));
    }

    /**
     * Получить все записи полива для конкретного растения.
     *
     * @param userPlantId ID растения
     * @return список записей полива растения
     */
    public List<WateringRecord> getWateringRecordsByUserPlantId(Integer userPlantId) {
        if (!userPlantRepository.existsById(userPlantId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Не существует растения пользователя с таким id.");
        }
        return wateringRecordRepository.findByUserPlantId(userPlantId);
    }

    /**
     * Получить отфильтрованные записи полива.
     *
     * @param plantId ID растения (необязательно)
     * @param dateFrom начальная дата (необязательно)
     * @param dateTo конечная дата (необязательно)
     * @return список записей полива
     */
    public List<WateringRecord> getFilteredRecords(Integer plantId, LocalDate dateFrom, LocalDate dateTo) {
        List<WateringRecord> records;

        if (plantId != null) {
            if (!userPlantRepository.existsById(plantId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Не существует растения пользователя с таким id.");
            }
            records = wateringRecordRepository.findByUserPlantId(plantId);
        } else {
            records = wateringRecordRepository.findAll();
        }

        if (dateFrom != null) {
            records = records.stream()
                    .filter(r -> !r.getDate().isBefore(dateFrom))
                    .collect(Collectors.toList());
        }

        if (dateTo != null) {
            records = records.stream()
                    .filter(r -> !r.getDate().isAfter(dateTo))
                    .collect(Collectors.toList());
        }

        records.sort((a, b) -> b.getDate().compareTo(a.getDate()));

        return records;
    }

    /**
     * Создать новую запись полива.
     *
     * @param wateringRecord объект записи
     * @return созданная запись с рассчитанной погрешностью
     * @throws ResponseStatusException если дата полива старше 7 дней, часть полей не заполнена, растение не найдено,
     * а также если запись полива на эту дату этого растения уже существует
     */
    public WateringRecord createWateringRecord(WateringRecord wateringRecord) {
        if (wateringRecord.getUserPlantId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "ID растения не может быть пустым.");
        }

        if (wateringRecord.getDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Дата не может быть пустой.");
        }

        if (wateringRecord.getTime() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Время не может быть пустым.");
        }

        if (wateringRecord.getVolumeWatering() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Объем полива не может быть пустым.");
        }

        if (!userPlantRepository.existsById(wateringRecord.getUserPlantId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Не существует растения с таким id.");
        }

        if (wateringRecordRepository.existsByUserPlantIdAndDate(
                wateringRecord.getUserPlantId(),
                wateringRecord.getDate())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Запись о поливе этого растения на эту дату уже существует.");
        }

        if (wateringRecord.getDate().isBefore(LocalDate.now().minusDays(7))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Данные устарели. Дата полива старше 7 дней.");
        }

        wateringRecord = autoCalculateErrorRate(wateringRecord);

        return wateringRecordRepository.save(wateringRecord);
    }

    /**
     * Обновить запись полива (частичное обновление).
     * При изменении volumeWatering пересчитывается errorRateK.
     *
     * @param id уникальный идентификатор записи
     * @param updatedWateringRecord объект с обновляемыми полями
     * @return обновлённая запись
     * @throws ResponseStatusException если дата полива старше 7 дней, запись полива не найдена,
     * а также если запись полива на эту дату этого растения уже существует
     */
    public WateringRecord updateWateringRecord(Integer id, WateringRecord updatedWateringRecord) {
        WateringRecord wateringRecord = wateringRecordRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не существует записи о поливе с таким id."
                ));

        LocalDate newDate = updatedWateringRecord.getDate();
        LocalDate oldDate = wateringRecord.getDate();

        if (newDate != null &&
                !newDate.equals(oldDate)) {
            if (wateringRecordRepository.existsByUserPlantIdAndDate(
                    wateringRecord.getUserPlantId(),
                    newDate)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Запись о поливе этого растения на эту дату уже существует.");
            }

            if (newDate.isBefore(LocalDate.now().minusDays(7))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Данные устарели. Дата полива старше 7 дней.");
            }

            wateringRecord.setDate(updatedWateringRecord.getDate());
        }

        if (updatedWateringRecord.getTime() != null) {
            wateringRecord.setTime(updatedWateringRecord.getTime());
        }

        if (updatedWateringRecord.getVolumeWatering() != null) {
            wateringRecord.setVolumeWatering(updatedWateringRecord.getVolumeWatering());
        }

        wateringRecord = autoCalculateErrorRate(wateringRecord);

        return wateringRecordRepository.save(wateringRecord);
    }

    /**
     * Удалить запись полива.
     *
     * @param id уникальный идентификатор записи
     * @throws ResponseStatusException если запись не найдена
     */
    public WateringRecord deleteWateringRecord(Integer id) {
        WateringRecord wateringRecord = wateringRecordRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не существует записи о поливе с таким id."));

        wateringRecordRepository.delete(wateringRecord);
        return wateringRecord;
    }

    /**
     * Получить последнюю запись полива для растения.
     *
     * @param userPlantId ID растения
     * @return последняя запись полива или null, если записей нет
     */
    public WateringRecord getLastWateringRecordByUserPlantId(Integer userPlantId) {
        return wateringRecordRepository
                .findFirstByUserPlantIdOrderByDateDesc(userPlantId)
                .orElse(null);
    }

    /**
     * Рассчитать рекомендуемый объём полива для растения.
     * Учитывает все параметры растения, условия микроклимата и предыдущие ошибки.
     *
     * @param userPlantId ID растения
     * @param wateringRecord последняя запись полива
     * @return рекомендуемый объём полива в мл
     * @throws ResponseStatusException если растение, тип растения, материал или условия не найдены
     */
    public Integer calculateWateringVolume(Integer userPlantId, WateringRecord wateringRecord) {
        UserPlant userPlant = userPlantRepository.findById(userPlantId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не существует растения с таким id."));

        PlantType plantType = plantTypeRepository.findById(userPlant.getPlantTypeId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не существует типа растения с таким id."));

        Material material = materialRepository.findById(userPlant.getMaterialId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не существует материала с таким id."));

        Conditions conditions = conditionsRepository.findFirstByUserIdOrderByDateDesc(
                        userPlant.getUserId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не найдены условия для этого пользователя."));

        int high = userPlant.getHigh() != null ? userPlant.getHigh() : 30;
        int potSize = userPlant.getPotSize() != null ? userPlant.getPotSize() : 15;
        int soilLoosenerk = userPlant.getSoilLoosenerk() != null ? userPlant.getSoilLoosenerk() : 10;
        int plantWateringK = plantType.getWateringK() != null ? plantType.getWateringK() : 50;
        int materialWateringK = material.getWateringK() != null ? material.getWateringK() : 50;
        int temperature = conditions.getTemperature();
        int humidity = conditions.getWatering();
        int errorRate = wateringRecord != null ? wateringRecord.getErrorRateK(): 0;

        double correctionFactor;

        if (plantWateringK <= 25) {
            correctionFactor = 0.2;
        } else if (plantWateringK <= 50) {
            correctionFactor = 0.4;
        } else if (plantWateringK <= 75) {
            correctionFactor = 0.6;
        } else {
            correctionFactor = 0.8;
        }

        int adjustedErrorRate = (int) (errorRate * correctionFactor);

        double baseVolume = potSize * 4;

        int maxCorrection = (int) (baseVolume * 0.3);

        if (adjustedErrorRate > maxCorrection) {
            adjustedErrorRate = maxCorrection;
        } else if (adjustedErrorRate < -maxCorrection) {
            adjustedErrorRate = -maxCorrection;
        }

        double highFactor = 1.0 + (high / 200.0);

        double plantTypeFactor = 0.5 + (plantWateringK / 100.0);

        double materialFactor = 0.7 + (materialWateringK / 100.0);

        double soilFactor = 1.0 + (soilLoosenerk / 300.0);

        double tempFactor = 1.0 + ((temperature - 20.0) / 100.0); // базовая температура 20
        if (tempFactor < 0.5) tempFactor = 0.5;

        double humidityFactor = 1.0 + ((50.0 - humidity) / 150.0); // базовая влажность 50%
        if (humidityFactor < 0.5) humidityFactor = 0.5;

        double calculatedVolume = (baseVolume * highFactor * plantTypeFactor *
                materialFactor * soilFactor * tempFactor * humidityFactor) + adjustedErrorRate;

        return Math.max(0, (int) Math.round(calculatedVolume));
    }

    /**
     * Автоматически рассчитать погрешность полива.
     *
     * @param wateringRecord запись полива
     * @return погрешность в мл
     * @throws ResponseStatusException часть данных полива не заполнена
     */
    public WateringRecord autoCalculateErrorRate(WateringRecord wateringRecord) {
        try {
            LocalDate recordDate = wateringRecord.getDate();
            Integer userPlantId = wateringRecord.getUserPlantId();

            WateringRecord previousRecord = wateringRecordRepository
                    .findFirstByUserPlantIdAndDateBeforeOrderByDateDesc(userPlantId, recordDate)
                    .orElse(null);

            Integer recommendedVolume = calculateWateringVolume(
                    userPlantId, previousRecord);

            Integer errorRate = recommendedVolume - wateringRecord.getVolumeWatering();

            wateringRecord.setErrorRateK(errorRate);

        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Некоторые данные для этого растения не заполнены.");
        }

        return wateringRecord;
    }
}
