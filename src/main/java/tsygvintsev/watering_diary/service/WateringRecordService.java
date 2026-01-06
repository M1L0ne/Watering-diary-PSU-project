package tsygvintsev.watering_diary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tsygvintsev.watering_diary.entity.WateringRecord;
import tsygvintsev.watering_diary.repository.WateringRecordRepository;
import tsygvintsev.watering_diary.repository.UserPlantRepository;

import java.util.List;

@Service
public class WateringRecordService {

    @Autowired
    private WateringRecordRepository wateringRecordRepository;

    @Autowired
    private UserPlantRepository userPlantRepository;

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

        if (wateringRecord.getErrorRateK() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Коэффициент ошибки не может быть пустым.");
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
}
