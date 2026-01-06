package tsygvintsev.watering_diary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tsygvintsev.watering_diary.entity.UserPlant;
import tsygvintsev.watering_diary.service.UserPlantService;

import java.util.List;

@RestController
@RequestMapping("/api/user-plants")
public class UserPlantController {

    @Autowired
    private UserPlantService userPlantService;

    @GetMapping
    public ResponseEntity<List<UserPlant>> getAllUserPlants() {
        return new ResponseEntity<>(userPlantService.getAllUserPlants(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserPlant> getUserPlantById(@PathVariable Integer id) {
        return new ResponseEntity<>(userPlantService.getUserPlantById(id), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserPlant>> getUserPlantsByUserId(@PathVariable Integer userId) {
        return new ResponseEntity<>(userPlantService.getUserPlantsByUserId(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserPlant> createUserPlant(@RequestBody UserPlant userPlant) {
        return new ResponseEntity<>(userPlantService.createUserPlant(userPlant), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserPlant> updateUserPlant(@PathVariable Integer id,
                                                     @RequestBody UserPlant userPlant) {
        return new ResponseEntity<>(userPlantService.updateUserPlant(id, userPlant), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserPlant> deleteUserPlant(@PathVariable Integer id) {
        return new ResponseEntity<>(userPlantService.deleteUserPlant(id), HttpStatus.OK);
    }
}
