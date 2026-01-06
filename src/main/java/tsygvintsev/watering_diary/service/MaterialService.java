package tsygvintsev.watering_diary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tsygvintsev.watering_diary.entity.Material;
import tsygvintsev.watering_diary.repository.MaterialRepository;

import java.util.List;

@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    public Material getMaterialById(Integer id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не существует материала с таким id."));
    }

    public Material createMaterial(Material material) {
        if (material.getName() == null || material.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Название материала не может быть пустым.");
        }

        if (materialRepository.existsByName(material.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Материал с таким названием уже существует.");
        }

        return materialRepository.save(material);
    }

    public Material updateMaterial(Integer id, Material updatedMaterial) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не существует материала с таким id."
                ));

        if (updatedMaterial.getName() != null && !updatedMaterial.getName().equals(material.getName())) {
            if (updatedMaterial.getName().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Название материала не может быть пустым.");
            }

            if (materialRepository.existsByName(updatedMaterial.getName())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Материал с таким названием уже существует.");
            }
            material.setName(updatedMaterial.getName());
        }

        if (updatedMaterial.getWateringK() != null) {
            material.setWateringK(updatedMaterial.getWateringK());
        }

        return materialRepository.save(material);
    }

    public Material deleteMaterial(Integer id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не существует материала с таким id."));

        materialRepository.delete(material);
        return material;
    }
}
