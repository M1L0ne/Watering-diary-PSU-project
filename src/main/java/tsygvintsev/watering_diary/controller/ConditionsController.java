package tsygvintsev.watering_diary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tsygvintsev.watering_diary.entity.Conditions;
import tsygvintsev.watering_diary.service.ConditionsService;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Контроллер для управления условиями микроклимата.
 * Хранит данные о температуре и влажности комнаты для расчёта полива.
 */
@RestController
@RequestMapping("/api/conditions")
public class ConditionsController {

    @Autowired
    private ConditionsService conditionsService;

    /**
     * Получить все записи условий микроклимата.
     *
     * @return ResponseEntity со списком условий и статусом 200 OK
     */
    @GetMapping
    public ResponseEntity<List<Conditions>> getAllConditions() {
        return new ResponseEntity<>(conditionsService.getAllConditions(), HttpStatus.OK);
    }

    /**
     * Получить условия микроклимата по ID.
     *
     * @param id уникальный идентификатор записи
     * @return ResponseEntity с данными условий и статусом 200 OK
     * @throws ResponseStatusException если запись не найдена
     */
    @GetMapping("/{id}")
    public ResponseEntity<Conditions> getConditionsById(@PathVariable Integer id) {
        return new ResponseEntity<>(conditionsService.getConditionsById(id), HttpStatus.OK);
    }

    /**
     * Получить все условия микроклимата конкретного пользователя.
     *
     * @param userId ID пользователя
     * @return ResponseEntity со списком условий и статусом 200 OK
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Conditions>> getConditionsByUserId(@PathVariable Integer userId) {
        return new ResponseEntity<>(conditionsService.getConditionsByUserId(userId), HttpStatus.OK);
    }

    /**
     * Создать новую запись условий микроклимата.
     *
     * @param conditions объект условий
     * @return ResponseEntity с созданной записью и статусом 201 CREATED
     * @throws ResponseStatusException если пользователь не найден
     */
    @PostMapping
    public ResponseEntity<Conditions> createConditions(@RequestBody Conditions conditions) {
        return new ResponseEntity<>(conditionsService.createConditions(conditions), HttpStatus.CREATED);
    }

    /**
     * Обновить условия микроклимата.
     *
     * @param id уникальный идентификатор записи
     * @param conditions объект с обновляемыми полями
     * @return ResponseEntity с обновлённой записью и статусом 200 OK
     * @throws ResponseStatusException если запись не найдена
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Conditions> updateConditions(@PathVariable Integer id,
                                                       @RequestBody Conditions conditions) {
        return new ResponseEntity<>(conditionsService.updateConditions(id, conditions), HttpStatus.OK);
    }

    /**
     * Удалить запись условий микроклимата.
     *
     * @param id уникальный идентификатор записи
     * @return ResponseEntity со статусом 204 NO_CONTENT
     * @throws ResponseStatusException если запись не найдена
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Conditions> deleteConditions(@PathVariable Integer id) {
        return new ResponseEntity<>(conditionsService.deleteConditions(id), HttpStatus.OK);
    }
}
