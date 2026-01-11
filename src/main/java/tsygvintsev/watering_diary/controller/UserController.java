package tsygvintsev.watering_diary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tsygvintsev.watering_diary.entity.User;
import tsygvintsev.watering_diary.service.UserService;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

/**
 * Контроллер для управления пользователями.
 * Предоставляет REST API endpoints для операций CRUD над пользователями.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Получить список всех пользователей.
     *
     * @return ResponseEntity со списком всех пользователей и статусом 200 OK
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    /**
     * Получить пользователя по ID.
     *
     * @param id уникальный идентификатор пользователя
     * @return ResponseEntity с данными пользователя и статусом 200 OK
     * @throws ResponseStatusException если пользователь с указанным ID не найден
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    /**
     * Авторизация пользователя.
     *
     * @param credentials JSON с login и password
     * @return ResponseEntity: 200 OK или 401
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String login = credentials.get("login");
        String password = credentials.get("password");

        if (userService.authenticate(login, password)) {
            Map<String, Object> response = Map.of(
                    "success", true,
                    "userId", userService.getUserByLogin(login).getId(),
                    "login", login
            );
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).body(Map.of("error", "Неверный логин/пароль"));
    }

    /**
     * Создать нового пользователя.
     *
     * @param user объект пользователя для создания
     * @return ResponseEntity с созданным пользователем и статусом 201 CREATED
     * @throws ResponseStatusException если пользователь с таким логином уже существует
     */
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userService.createUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    /**
     * Обновить данные пользователя.
     *
     * @param id уникальный идентификатор пользователя
     * @param user объект с обновляемыми полями
     * @return ResponseEntity с обновлённым пользователем и статусом 200 OK
     * @throws ResponseStatusException если пользователь не найден или новый логин уже занят
     */
    @PatchMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User user) {
        User updatedUser = userService.updateUser(id, user);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    /**
     * Удалить пользователя.
     *
     * @param id уникальный идентификатор пользователя
     * @return ResponseEntity со статусом 204 NO_CONTENT
     * @throws ResponseStatusException если пользователь не найден
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
