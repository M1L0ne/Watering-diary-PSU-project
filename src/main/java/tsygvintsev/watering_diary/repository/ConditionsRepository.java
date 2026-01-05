package tsygvintsev.watering_diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tsygvintsev.watering_diary.entity.Conditions;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConditionsRepository extends JpaRepository<Conditions, Integer> {
    boolean existsByUserIdAndDate(Integer userId, LocalDate date);
    List<Conditions> findByUserId(Integer userId);
}
