package tsygvintsev.watering_diary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tsygvintsev.watering_diary.entity.Material;
import tsygvintsev.watering_diary.repository.MaterialRepository;

import java.util.List;

/**
 * Сервис для управления материалами горшков.
 * Предоставляет доступ к справочнику материалов.
 */
@Service
public class MaterialService {

    @Autowired
    private MaterialRepository materialRepository;

    /**
     * Получить все материалы.
     *
     * @return список материалов
     */
    public List<Material> getAllMaterials() {
        return materialRepository.findAll();
    }

    /**
     * Получить материал по ID.
     *
     * @param id уникальный идентификатор материала
     * @return найденный материал
     * @throws ResponseStatusException если материал не найден
     */
    public Material getMaterialById(Integer id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не существует материала с таким id."));
    }

    /**
     * Создать новый материал.
     *
     * @param material объект материала
     * @return созданная запись
     * @throws ResponseStatusException название материала пустое,
     * материал с таким названием уже существует
     */
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

    /**
     * Обновить материал.
     *
     * @param id уникальный идентификатор записи
     * @param updatedMaterial объект с обновляемыми полями
     * @return обновлённая запись
     * @throws ResponseStatusException если материал не найден
     * или материал с таким названием уже существует
     */
    public Material updateMaterial(Integer id, Material updatedMaterial) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не существует материала с таким id."
                ));

        if (updatedMaterial.getName() != null && !updatedMaterial.getName().equals(material.getName())) {
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

    /**
     * Удалить материал.
     *
     * @param id уникальный идентификатор материала
     * @throws ResponseStatusException если материал не найден
     */
    public Material deleteMaterial(Integer id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не существует материала с таким id."));

        materialRepository.delete(material);
        return material;
    }
}
