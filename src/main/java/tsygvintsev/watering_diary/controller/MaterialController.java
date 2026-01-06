package tsygvintsev.watering_diary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tsygvintsev.watering_diary.entity.Material;
import tsygvintsev.watering_diary.service.MaterialService;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @GetMapping
    public ResponseEntity<List<Material>> getAllMaterials() {
        return new ResponseEntity<>(materialService.getAllMaterials(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Material> getMaterialById(@PathVariable Integer id) {
        return new ResponseEntity<>(materialService.getMaterialById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Material> createMaterial(@RequestBody Material material) {
        return new ResponseEntity<>(materialService.createMaterial(material), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Material> updateMaterial(@PathVariable Integer id,
                                                   @RequestBody Material material) {
        return new ResponseEntity<>(materialService.updateMaterial(id, material), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Material> deleteMaterial(@PathVariable Integer id) {
        return new ResponseEntity<>(materialService.deleteMaterial(id), HttpStatus.OK);
    }
}
