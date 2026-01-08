package tsygvintsev.watering_diary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tsygvintsev.watering_diary.entity.PlantType;
import tsygvintsev.watering_diary.service.PlantTypeService;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Контроллер для управления типами растений.
 * Типы растений используются для расчёта объёма полива.
 */
@RestController
@RequestMapping("/api/plant-types")
public class PlantTypeController {

    @Autowired
    private PlantTypeService plantTypeService;

    /**
     * Получить все типы растений.
     *
     * @return ResponseEntity со списком типов и статусом 200 OK
     */
    @GetMapping
    public ResponseEntity<List<PlantType>> getAllPlantTypes() {
        return new ResponseEntity<>(plantTypeService.getAllPlantTypes(), HttpStatus.OK);
    }

    /**
     * Получить тип растения по ID.
     *
     * @param id уникальный идентификатор типа
     * @return ResponseEntity с данными типа и статусом 200 OK
     * @throws ResponseStatusException если тип не найден
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlantType> getPlantTypeById(@PathVariable Integer id) {
        return new ResponseEntity<>(plantTypeService.getPlantTypeById(id), HttpStatus.OK);
    }

    /**
     * Создать новый тип растения.
     *
     * @param plantType объект типа растения для создания
     * @return ResponseEntity с созданным типом растения и статусом 201 CREATED
     * @throws ResponseStatusException если тип растения с таким названием уже существует
     */
    @PostMapping
    public ResponseEntity<PlantType> createPlantType(@RequestBody PlantType plantType) {
        return new ResponseEntity<>(plantTypeService.createPlantType(plantType), HttpStatus.CREATED);
    }

    /**
     * Обновить данные типа растения.
     *
     * @param id уникальный идентификатор типа растения
     * @param plantType объект с обновляемыми полями
     * @return ResponseEntity с обновлённым типом растения и статусом 200 OK
     * @throws ResponseStatusException если тип растения не найден или новое название уже занято
     */
    @PatchMapping("/{id}")
    public ResponseEntity<PlantType> updatePlantType(@PathVariable Integer id,
                                                     @RequestBody PlantType plantType) {
        return new ResponseEntity<>(plantTypeService.updatePlantType(id, plantType), HttpStatus.OK);
    }

    /**
     * Удалить тип растения.
     *
     * @param id уникальный идентификатор типа растения
     * @return ResponseEntity со статусом 204 NO_CONTENT
     * @throws ResponseStatusException если тип растения не найден
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<PlantType> deletePlantType(@PathVariable Integer id) {
        return new ResponseEntity<>(plantTypeService.deletePlantType(id), HttpStatus.NO_CONTENT);
    }
}
