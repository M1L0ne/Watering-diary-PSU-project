package tsygvintsev.watering_diary.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tsygvintsev.watering_diary.entity.WateringRecord;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WateringRecordRepository extends JpaRepository<WateringRecord, Integer> {
    boolean existsByUserPlantIdAndDate(Integer userPlantId, LocalDate date);
    List<WateringRecord> findByUserPlantId(Integer userPlantId);
}
