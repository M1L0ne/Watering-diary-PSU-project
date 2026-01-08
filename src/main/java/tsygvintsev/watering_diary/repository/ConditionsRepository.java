package tsygvintsev.watering_diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tsygvintsev.watering_diary.entity.Conditions;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConditionsRepository extends JpaRepository<Conditions, Integer> {
    boolean existsByUserIdAndDate(Integer userId, LocalDate date);
    List<Conditions> findByUserId(Integer userId);
    Optional<Conditions> findFirstByUserIdOrderByDateDesc(Integer userId);
}
