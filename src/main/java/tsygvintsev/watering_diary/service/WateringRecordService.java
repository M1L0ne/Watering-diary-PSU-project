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
import java.util.List;

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

    public List<WateringRecord> getAllWateringRecords() {
        return wateringRecordRepository.findAll();
    }

    public WateringRecord getWateringRecordById(Integer id) {
        return wateringRecordRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не существует записи о поливе с таким id."));
    }

    public List<WateringRecord> getWateringRecordsByUserPlantId(Integer userPlantId) {
        if (!userPlantRepository.existsById(userPlantId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Не существует растения пользователя с таким id.");
        }
        return wateringRecordRepository.findByUserPlantId(userPlantId);
    }

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

        try {
            Integer recommendedVolume = calculateWateringVolume(wateringRecord.getUserPlantId());

            Integer errorRate =  recommendedVolume - wateringRecord.getVolumeWatering();

            wateringRecord.setErrorRateK(errorRate);

        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Некоторые данные для этого растения не заполнены.");
        }

        return wateringRecordRepository.save(wateringRecord);
    }

    public WateringRecord updateWateringRecord(Integer id, WateringRecord updatedWateringRecord) {
        WateringRecord wateringRecord = wateringRecordRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не существует записи о поливе с таким id."
                ));

        if (updatedWateringRecord.getDate() != null &&
                !updatedWateringRecord.getDate().equals(wateringRecord.getDate())) {
            if (wateringRecordRepository.existsByUserPlantIdAndDate(
                    wateringRecord.getUserPlantId(),
                    updatedWateringRecord.getDate())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Запись о поливе этого растения на эту дату уже существует.");
            }
            wateringRecord.setDate(updatedWateringRecord.getDate());
        }

        if (updatedWateringRecord.getTime() != null) {
            wateringRecord.setTime(updatedWateringRecord.getTime());
        }
        if (updatedWateringRecord.getVolumeWatering() != null) {
            wateringRecord.setVolumeWatering(updatedWateringRecord.getVolumeWatering());
        }
        if (updatedWateringRecord.getErrorRateK() != null) {
            wateringRecord.setErrorRateK(updatedWateringRecord.getErrorRateK());
        }

        return wateringRecordRepository.save(wateringRecord);
    }

    public WateringRecord deleteWateringRecord(Integer id) {
        WateringRecord wateringRecord = wateringRecordRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не существует записи о поливе с таким id."));

        wateringRecordRepository.delete(wateringRecord);
        return wateringRecord;
    }

    public Integer calculateWateringVolume(Integer userPlantId) {
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

        Integer high = userPlant.getHigh() != null ? userPlant.getHigh() : 30; // см
        Integer potSize = userPlant.getPotSize() != null ? userPlant.getPotSize() : 15; // см
        Integer soilLoosenerk = userPlant.getSoilLoosenerk() != null ? userPlant.getSoilLoosenerk() : 10; // %
        Integer plantWateringK = plantType.getWateringK() != null ? plantType.getWateringK() : 50; // 1-100
        Integer materialWateringK = material.getWateringK() != null ? material.getWateringK() : 50; // 1-100
        Integer temperature = conditions.getTemperature(); // градусы
        Integer humidity = conditions.getWatering(); // % (0-100)

        double baseVolume = Math.PI * Math.pow(potSize / 2.0, 2) * (potSize * 0.8) / 10.0; // мл

        double highFactor = 1.0 + (high / 100.0);

        double plantTypeFactor = plantWateringK / 50.0;

        double materialFactor = materialWateringK / 50.0;

        double soilFactor = 1.0 + (soilLoosenerk / 100.0);

        double tempFactor = 1.0 + ((temperature - 20.0) / 50.0); // базовая температура 20
        if (tempFactor < 0.5) tempFactor = 0.5;

        double humidityFactor = 1.0 + ((50.0 - humidity) / 100.0); // базовая влажность 50%
        if (humidityFactor < 0.5) humidityFactor = 0.5;

        double calculatedVolume = baseVolume * highFactor * plantTypeFactor *
                materialFactor * soilFactor * tempFactor * humidityFactor;

        return (int) Math.round(calculatedVolume);
    }
}
