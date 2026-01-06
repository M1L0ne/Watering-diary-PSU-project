package tsygvintsev.watering_diary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tsygvintsev.watering_diary.entity.PlantType;
import tsygvintsev.watering_diary.service.PlantTypeService;

import java.util.List;

@RestController
@RequestMapping("/api/plant-types")
public class PlantTypeController {

    @Autowired
    private PlantTypeService plantTypeService;

    @GetMapping
    public ResponseEntity<List<PlantType>> getAllPlantTypes() {
        return new ResponseEntity<>(plantTypeService.getAllPlantTypes(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlantType> getPlantTypeById(@PathVariable Integer id) {
        return new ResponseEntity<>(plantTypeService.getPlantTypeById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PlantType> createPlantType(@RequestBody PlantType plantType) {
        return new ResponseEntity<>(plantTypeService.createPlantType(plantType), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PlantType> updatePlantType(@PathVariable Integer id,
                                                     @RequestBody PlantType plantType) {
        return new ResponseEntity<>(plantTypeService.updatePlantType(id, plantType), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PlantType> deletePlantType(@PathVariable Integer id) {
        return new ResponseEntity<>(plantTypeService.deletePlantType(id), HttpStatus.OK);
    }
}
