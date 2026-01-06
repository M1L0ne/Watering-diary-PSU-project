package tsygvintsev.watering_diary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tsygvintsev.watering_diary.entity.User;
import tsygvintsev.watering_diary.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не существует пользователя с таким id."));
    }

    public User createUser(User user) {
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Логин не может быть пустым.");
        }

        if (userRepository.existsByLogin(user.getLogin())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Пользователь с таким логином уже существует.");
        }

        return userRepository.save(user);
    }


    public User updateUser(Integer id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не существует пользователя с таким id."
                ));

        if (updatedUser.getPassword() != null) {
            user.setPassword(updatedUser.getPassword());
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

    public User deleteUser(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
                "Не существует пользователя с таким id."));

        userRepository.delete(user);

        return user;
    }
}
