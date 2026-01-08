package tsygvintsev.watering_diary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tsygvintsev.watering_diary.entity.PlantType;
import tsygvintsev.watering_diary.repository.PlantTypeRepository;

import java.util.List;

/**
 * Сервис для управления типами растений.
 * Предоставляет доступ к справочнику типов растений.
 */
@Service
public class PlantTypeService {

    @Autowired
    private PlantTypeRepository plantTypeRepository;

    /**
     * Получить все типы растений, отсортированные по названию.
     *
     * @return список типов растений
     */
    public List<PlantType> getAllPlantTypes() {
        return plantTypeRepository.findAllByOrderByNameAsc();
    }

    /**
     * Получить тип растения по ID.
     *
     * @param id уникальный идентификатор типа
     * @return найденный тип растения
     * @throws ResponseStatusException если тип не найден
     */
    public PlantType getPlantTypeById(Integer id) {
        return plantTypeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не существует типа растения с таким id."));
    }

    /**
     * Создать новый тип растения.
     *
     * @param plantType объект типа растения
     * @return созданная запись
     * @throws ResponseStatusException название типа растения пустое,
     * тип растения с таким названием уже существует
     */
    public PlantType createPlantType(PlantType plantType) {
        if (plantType.getName() == null || plantType.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Название типа растения не может быть пустым.");
        }

        if (plantTypeRepository.existsByName(plantType.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Тип растения с таким названием уже существует.");
        }

        return plantTypeRepository.save(plantType);
    }

    /**
     * Обновить тип растения.
     *
     * @param id уникальный идентификатор записи
     * @param updatedPlantType объект с обновляемыми полями
     * @return обновлённая запись
     * @throws ResponseStatusException если тип растения не найден
     * или тип растения с таким названием уже существует
     */
    public PlantType updatePlantType(Integer id, PlantType updatedPlantType) {
        PlantType plantType = plantTypeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не существует типа растения с таким id."
                ));

        if (updatedPlantType.getName() != null && !updatedPlantType.getName().equals(plantType.getName())) {
            if (plantTypeRepository.existsByName(updatedPlantType.getName())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Тип растения с таким названием уже существует.");
            }
            plantType.setName(updatedPlantType.getName());
        }

        if (updatedPlantType.getDescription() != null) {
            plantType.setDescription(updatedPlantType.getDescription());
        }
        if (updatedPlantType.getWateringK() != null) {
            plantType.setWateringK(updatedPlantType.getWateringK());
        }

        return plantTypeRepository.save(plantType);
    }

    /**
     * Удалить тип растения.
     *
     * @param id уникальный идентификатор типа растения
     * @throws ResponseStatusException если тип растения не найден
     */
    public PlantType deletePlantType(Integer id) {
        PlantType plantType = plantTypeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не существует типа растения с таким id."));

        plantTypeRepository.delete(plantType);
        return plantType;
    }
}
