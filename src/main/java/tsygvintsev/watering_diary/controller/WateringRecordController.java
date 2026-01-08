package tsygvintsev.watering_diary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tsygvintsev.watering_diary.entity.WateringRecord;
import tsygvintsev.watering_diary.service.WateringRecordService;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Контроллер для управления записями полива растений.
 * Предоставляет REST API endpoints для учёта полива и расчёта рекомендаций.
 */
@RestController
@RequestMapping("/api/watering-records")
public class WateringRecordController {

    @Autowired
    private WateringRecordService wateringRecordService;

    /**
     * Получить все записи полива.
     *
     * @return ResponseEntity со списком записей и статусом 200 OK
     */
    @GetMapping
    public ResponseEntity<List<WateringRecord>> getAllWateringRecords() {
        return new ResponseEntity<>(wateringRecordService.getAllWateringRecords(), HttpStatus.OK);
    }

    /**
     * Получить запись полива по ID.
     *
     * @param id уникальный идентификатор записи
     * @return ResponseEntity с данными записи и статусом 200 OK
     * @throws ResponseStatusException если запись не найдена (404 NOT_FOUND)
     */
    @GetMapping("/{id}")
    public ResponseEntity<WateringRecord> getWateringRecordById(@PathVariable Integer id) {
        return new ResponseEntity<>(wateringRecordService.getWateringRecordById(id), HttpStatus.OK);
    }

    /**
     * Получить все записи полива для конкретного растения пользователя.
     *
     * @param userPlantId ID растения
     * @return ResponseEntity со списком записей полива и статусом 200 OK
     */
    @GetMapping("/plant/{userPlantId}")
    public ResponseEntity<List<WateringRecord>> getWateringRecordsByUserPlantId(@PathVariable Integer userPlantId) {
        return new ResponseEntity<>(wateringRecordService.getWateringRecordsByUserPlantId(userPlantId), HttpStatus.OK);
    }

    /**
     * Создать новую запись полива.
     *
     * @param wateringRecord объект записи
     * @return ResponseEntity с созданной записью и статусом 201 CREATED
     * @throws ResponseStatusException если дата полива старше 7 дней (400 BAD_REQUEST)
     *         или растение не найдено (404 NOT_FOUND)
     */
    @PostMapping
    public ResponseEntity<WateringRecord> createWateringRecord(@RequestBody WateringRecord wateringRecord) {
        return new ResponseEntity<>(wateringRecordService.createWateringRecord(wateringRecord), HttpStatus.CREATED);
    }

    /**
     * Обновить запись полива.
     *
     * @param id уникальный идентификатор записи
     * @param wateringRecord объект с обновляемыми полями
     * @return ResponseEntity с обновлённой записью и статусом 200 OK
     * @throws ResponseStatusException если запись не найдена или новая дата старше 7 дней
     */
    @PatchMapping("/{id}")
    public ResponseEntity<WateringRecord> updateWateringRecord(@PathVariable Integer id,
                                                               @RequestBody WateringRecord wateringRecord) {
        return new ResponseEntity<>(wateringRecordService.updateWateringRecord(id, wateringRecord), HttpStatus.OK);
    }

    /**
     * Удалить запись полива.
     *
     * @param id уникальный идентификатор записи
     * @return ResponseEntity со статусом 204 NO_CONTENT
     * @throws ResponseStatusException если запись не найдена
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<WateringRecord> deleteWateringRecord(@PathVariable Integer id) {
        return new ResponseEntity<>(wateringRecordService.deleteWateringRecord(id), HttpStatus.OK);
    }

    /**
     * Рассчитать рекомендуемый объём полива для растения.
     * Учитывает параметры растения, текущие условия микроклимата и предыдущие ошибки полива.
     *
     * @param userPlantId ID растения для расчёта
     * @return ResponseEntity с объектом, содержащим рекомендуемый объём (мл) и статусом 200 OK
     * @throws ResponseStatusException если растение, тип растения, материал или условия не найдены
     */
    @GetMapping("/calculate")
    public ResponseEntity<Map<String, Object>> calculateWateringVolume(
            @RequestParam Integer userPlantId) {

        WateringRecord lastRecord = wateringRecordService.getLastWateringRecordByUserPlantId(userPlantId);

        Integer recommendedVolume = wateringRecordService.calculateWateringVolume(userPlantId, lastRecord);

        Map<String, Object> response = new HashMap<>();
        response.put("userPlantId", userPlantId);
        response.put("recommendedVolume", recommendedVolume);
        response.put("unit", "мл");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
