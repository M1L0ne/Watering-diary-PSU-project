package tsygvintsev.watering_diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tsygvintsev.watering_diary.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByLogin(String login);
    Optional<User> findUserById(Integer id);
}
