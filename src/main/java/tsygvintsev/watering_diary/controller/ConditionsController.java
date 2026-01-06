package tsygvintsev.watering_diary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tsygvintsev.watering_diary.entity.Conditions;
import tsygvintsev.watering_diary.service.ConditionsService;

import java.util.List;

@RestController
@RequestMapping("/api/conditions")
public class ConditionsController {

    @Autowired
    private ConditionsService conditionsService;

    @GetMapping
    public ResponseEntity<List<Conditions>> getAllConditions() {
        return new ResponseEntity<>(conditionsService.getAllConditions(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Conditions> getConditionsById(@PathVariable Integer id) {
        return new ResponseEntity<>(conditionsService.getConditionsById(id), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Conditions>> getConditionsByUserId(@PathVariable Integer userId) {
        return new ResponseEntity<>(conditionsService.getConditionsByUserId(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Conditions> createConditions(@RequestBody Conditions conditions) {
        return new ResponseEntity<>(conditionsService.createConditions(conditions), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Conditions> updateConditions(@PathVariable Integer id,
                                                       @RequestBody Conditions conditions) {
        return new ResponseEntity<>(conditionsService.updateConditions(id, conditions), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Conditions> deleteConditions(@PathVariable Integer id) {
        return new ResponseEntity<>(conditionsService.deleteConditions(id), HttpStatus.OK);
    }
}
