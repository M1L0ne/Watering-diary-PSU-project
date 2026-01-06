package tsygvintsev.watering_diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tsygvintsev.watering_diary.entity.Material;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {
    boolean existsByName(String name);
}
