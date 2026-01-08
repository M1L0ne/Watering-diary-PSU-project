package tsygvintsev.watering_diary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tsygvintsev.watering_diary.entity.WateringRecord;
import tsygvintsev.watering_diary.service.WateringRecordService;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/watering-records")
public class WateringRecordController {

    @Autowired
    private WateringRecordService wateringRecordService;

    @GetMapping
    public ResponseEntity<List<WateringRecord>> getAllWateringRecords() {
        return new ResponseEntity<>(wateringRecordService.getAllWateringRecords(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WateringRecord> getWateringRecordById(@PathVariable Integer id) {
        return new ResponseEntity<>(wateringRecordService.getWateringRecordById(id), HttpStatus.OK);
    }

    @GetMapping("/plant/{userPlantId}")
    public ResponseEntity<List<WateringRecord>> getWateringRecordsByUserPlantId(@PathVariable Integer userPlantId) {
        return new ResponseEntity<>(wateringRecordService.getWateringRecordsByUserPlantId(userPlantId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<WateringRecord> createWateringRecord(@RequestBody WateringRecord wateringRecord) {
        return new ResponseEntity<>(wateringRecordService.createWateringRecord(wateringRecord), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<WateringRecord> updateWateringRecord(@PathVariable Integer id,
                                                               @RequestBody WateringRecord wateringRecord) {
        return new ResponseEntity<>(wateringRecordService.updateWateringRecord(id, wateringRecord), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<WateringRecord> deleteWateringRecord(@PathVariable Integer id) {
        return new ResponseEntity<>(wateringRecordService.deleteWateringRecord(id), HttpStatus.OK);
    }

    @GetMapping("/calculate")
    public ResponseEntity<Map<String, Object>> calculateWateringVolume(
            @RequestParam Integer userPlantId) {

        WateringRecord lastRecord = wateringRecordService.getLastWateringRecordByUserPlantId(userPlantId);

        Integer recommendedVolume = wateringRecordService.calculateWateringVolume(userPlantId, lastRecord);

        Map<String, Object> response = new HashMap<>();
        response.put("userPlantId", userPlantId);
        response.put("recommendedVolume", recommendedVolume);
        response.put("unit", "мл");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
