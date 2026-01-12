package tsygvintsev.watering_diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tsygvintsev.watering_diary.entity.UserPlant;

import java.util.List;

/**
 * Репозиторий для работы с растениями пользователей.
 */
@Repository
public interface UserPlantRepository extends JpaRepository<UserPlant, Integer> {
    boolean existsByNameAndPlantTypeIdAndUserId(String name, Integer plantTypeId, Integer userId);
    List<UserPlant> findByUserId(Integer userId);
    List<UserPlant> findByUserIdOrderById(Integer userId);
}
