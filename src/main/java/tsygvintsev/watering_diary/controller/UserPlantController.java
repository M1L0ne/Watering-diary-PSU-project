package tsygvintsev.watering_diary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tsygvintsev.watering_diary.entity.UserPlant;
import tsygvintsev.watering_diary.service.UserPlantService;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Контроллер для управления растениями пользователей.
 * Предоставляет REST API endpoints для операций CRUD над растениями.
 */
@RestController
@RequestMapping("/api/user-plants")
public class UserPlantController {

    @Autowired
    private UserPlantService userPlantService;

    /**
     * Получить список всех растений, отсортированных по названию.
     *
     * @return ResponseEntity со списком растений и статусом 200 OK
     */
    @GetMapping
    public ResponseEntity<List<UserPlant>> getAllUserPlants() {
        return new ResponseEntity<>(userPlantService.getAllUserPlants(), HttpStatus.OK);
    }

    /**
     * Получить растение по ID.
     *
     * @param id уникальный идентификатор растения
     * @return ResponseEntity с данными растения и статусом 200 OK
     * @throws ResponseStatusException если растение не найдено
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserPlant> getUserPlantById(@PathVariable Integer id) {
        return new ResponseEntity<>(userPlantService.getUserPlantById(id), HttpStatus.OK);
    }

    /**
     * Получить все растения конкретного пользователя.
     *
     * @param userId ID пользователя
     * @return ResponseEntity со списком растений пользователя и статусом 200 OK
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserPlant>> getUserPlantsByUserId(@PathVariable Integer userId) {
        return new ResponseEntity<>(userPlantService.getUserPlantsByUserId(userId), HttpStatus.OK);
    }

    /**
     * Создать новое растение.
     *
     * @param userPlant объект растения
     * @return ResponseEntity с созданным растением и статусом 201 CREATED
     * @throws ResponseStatusException если пользователь, тип растения или материал не найдены
     */
    @PostMapping
    public ResponseEntity<UserPlant> createUserPlant(@RequestBody UserPlant userPlant) {
        return new ResponseEntity<>(userPlantService.createUserPlant(userPlant), HttpStatus.CREATED);
    }

    /**
     * Обновить данные растения.
     *
     * @param id уникальный идентификатор растения
     * @param userPlant объект с обновляемыми полями
     * @return ResponseEntity с обновлённым растением и статусом 200 OK
     * @throws ResponseStatusException если растение не найдено
     */
    @PatchMapping("/{id}")
    public ResponseEntity<UserPlant> updateUserPlant(@PathVariable Integer id,
                                                     @RequestBody UserPlant userPlant) {
        return new ResponseEntity<>(userPlantService.updateUserPlant(id, userPlant), HttpStatus.OK);
    }

    /**
     * Удалить растение.
     *
     * @param id уникальный идентификатор растения
     * @return ResponseEntity со статусом 204 NO_CONTENT
     * @throws ResponseStatusException если растение не найдено
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<UserPlant> deleteUserPlant(@PathVariable Integer id) {
        return new ResponseEntity<>(userPlantService.deleteUserPlant(id), HttpStatus.OK);
    }
}
