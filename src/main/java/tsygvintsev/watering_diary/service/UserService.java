package tsygvintsev.watering_diary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tsygvintsev.watering_diary.entity.User;
import tsygvintsev.watering_diary.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

/**
 * Сервис для управления пользователями.
 * Содержит бизнес-логику для операций CRUD над пользователями.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Получить список всех пользователей.
     *
     * @return список всех пользователей
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Получить пользователя по ID.
     *
     * @param id уникальный идентификатор пользователя
     * @return найденный пользователь
     * @throws ResponseStatusException если пользователь не найден
     */
    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не существует пользователя с таким id."));
    }

    /**
     * Создать нового пользователя.
     *
     * @param user объект пользователя
     * @return созданный пользователь
     * @throws ResponseStatusException если пользователь с таким логином уже существует или логин пуст
     */
    public User createUser(User user) {
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Логин не может быть пустым.");
        }

        if (userRepository.existsByLogin(user.getLogin())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Пользователь с таким логином уже существует.");
        }

        if (user.getPassword().length() < 6) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Пароль должен быть не меньше 6 символов.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    /**
     * Обновить данные пользователя.
     *
     * @param id уникальный идентификатор пользователя
     * @param updatedUser объект с обновляемыми полями
     * @return обновлённый пользователь
     * @throws ResponseStatusException если пользователь не найден
     */
    public User updateUser(Integer id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не существует пользователя с таким id."
                ));

        if (updatedUser.getPassword() != null) {
            if (updatedUser.getPassword().length() < 6) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Пароль должен быть не меньше 6 символов.");
            }
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        if (updatedUser.getName() != null) {
            user.setName(updatedUser.getName());
        }
        if (updatedUser.getSurname() != null) {
            user.setSurname(updatedUser.getSurname());
        }
        if (updatedUser.getPatronymic() != null) {
            user.setPatronymic(updatedUser.getPatronymic());
        }

        return userRepository.save(user);
    }

    /**
     * Удалить пользователя.
     *
     * @param id уникальный идентификатор пользователя
     * @throws ResponseStatusException если пользователь не найден
     */
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
                "Не существует пользователя с таким id."));

        userRepository.delete(user);
    }
}
