package tsygvintsev.watering_diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tsygvintsev.watering_diary.entity.PlantType;

@Repository
public interface PlantTypeRepository extends JpaRepository<PlantType, Integer> {
    boolean existsByName(String name);
}
