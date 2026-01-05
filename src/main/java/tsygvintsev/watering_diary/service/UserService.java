package tsygvintsev.watering_diary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
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
        return userRepository.findUserById(id)
                .orElse(null);
    }

    public User createUser(User user) {
        String login = user.getLogin();

        if (userRepository.findById(login).orElse(null) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Пользователь с таким логином уже существует.");
        }

        return userRepository.save(user);
    }

    public User updateUser(Integer id, User updatedUser) {
        User user = userRepository.findUserById(id)
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
        User user = userRepository.findUserById(id).orElse(null);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Не существует пользователя с таким id.");
        }

        userRepository.delete(user);

        return user;
    }
}
