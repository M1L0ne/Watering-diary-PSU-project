package tsygvintsev.watering_diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tsygvintsev.watering_diary.entity.Material;

/**
 * Репозиторий для работы с материалами горшков.
 */
@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {
    boolean existsByName(String name);
}
