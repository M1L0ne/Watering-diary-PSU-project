package tsygvintsev.watering_diary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tsygvintsev.watering_diary.entity.UserPlant;
import tsygvintsev.watering_diary.repository.UserPlantRepository;
import tsygvintsev.watering_diary.repository.UserRepository;
import tsygvintsev.watering_diary.repository.PlantTypeRepository;
import tsygvintsev.watering_diary.repository.MaterialRepository;

import java.util.List;

/**
 * Сервис для управления растениями пользователей.
 * Содержит бизнес-логику для операций CRUD над растениями.
 */
@Service
public class UserPlantService {
    @Autowired
    private UserPlantRepository userPlantRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlantTypeRepository plantTypeRepository;

    @Autowired
    private MaterialRepository materialRepository;

    /**
     * Получить список всех растений пользователей.
     *
     * @return список всех растений
     */
    public List<UserPlant> getAllUserPlants() {
        return userPlantRepository.findAll();
    }

    /**
     * Получить растение пользователя по ID.
     *
     * @param id уникальный идентификатор растения
     * @return найденное растение
     * @throws ResponseStatusException если растение не найдено
     */
    public UserPlant getUserPlantById(Integer id) {
        return userPlantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не существует растения с таким id."));
    }

    /**
     * Получить все растения конкретного пользователя.
     *
     * @param userId ID пользователя
     * @return список растений пользователя
     */
    public List<UserPlant> getUserPlantsByUserId(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Не существует пользователя с таким id.");
        }
        return userPlantRepository.findByUserId(userId);
    }

    /**
     * Создать новое растение.
     *
     * @param userPlant объект растения
     * @return созданное растение
     * @throws ResponseStatusException если пользователь, тип растения или материал не найдены,
     * а также если пусты или уже заняты
     */
    public UserPlant createUserPlant(UserPlant userPlant) {
        if (userPlant.getName() == null || userPlant.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Название растения не может быть пустым.");
        }

        if (userPlant.getPlantTypeId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Тип растения не может быть пустым.");
        }

        if (userPlant.getUserId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "ID пользователя не может быть пустым.");
        }

        if (userPlant.getMaterialId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "ID материала не может быть пустым.");
        }

        if (!userRepository.existsById(userPlant.getUserId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Не существует пользователя с таким id.");
        }

        if (!plantTypeRepository.existsById(userPlant.getPlantTypeId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Не существует типа растения с таким id.");
        }

        if (!materialRepository.existsById(userPlant.getMaterialId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Не существует материала с таким id.");
        }

        if (userPlantRepository.existsByNameAndPlantTypeIdAndUserId(
                userPlant.getName(),
                userPlant.getPlantTypeId(),
                userPlant.getUserId())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "У этого пользователя уже есть растение с таким названием и типом.");
        }

        return userPlantRepository.save(userPlant);
    }

    /**
     * Обновить данные растения.
     *
     * @param id уникальный идентификатор растения
     * @param updatedUserPlant объект с обновляемыми полями
     * @return обновлённое растение
     * @throws ResponseStatusException если растение, тип материала или тип растения не найдены,
     * а также если пусто название растения или у пользователя оно уже есть
     */
    public UserPlant updateUserPlant(Integer id, UserPlant updatedUserPlant) {
        UserPlant userPlant = userPlantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не существует растения с таким id."
                ));

        String newName = updatedUserPlant.getName() != null ?
                updatedUserPlant.getName() : userPlant.getName();
        Integer newPlantTypeId = updatedUserPlant.getPlantTypeId() != null ?
                updatedUserPlant.getPlantTypeId() : userPlant.getPlantTypeId();
        Integer newUserId = userPlant.getUserId();

        if (updatedUserPlant.getName() != null && updatedUserPlant.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Название растения не может быть пустым.");
        }

        if (updatedUserPlant.getPlantTypeId() != null &&
                !plantTypeRepository.existsById(updatedUserPlant.getPlantTypeId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Не существует типа растения с таким id.");
        }

        if (updatedUserPlant.getMaterialId() != null &&
                !materialRepository.existsById(updatedUserPlant.getMaterialId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Не существует материала с таким id.");
        }

        boolean keyFieldsChanged =
                (updatedUserPlant.getName() != null && !updatedUserPlant.getName().equals(userPlant.getName())) ||
                        (updatedUserPlant.getPlantTypeId() != null && !updatedUserPlant.getPlantTypeId().equals(userPlant.getPlantTypeId()));

        if (keyFieldsChanged && userPlantRepository.existsByNameAndPlantTypeIdAndUserId(
                newName, newPlantTypeId, newUserId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "У этого пользователя уже есть растение с таким названием и типом.");
        }

        if (updatedUserPlant.getName() != null) {
            userPlant.setName(updatedUserPlant.getName());
        }
        if (updatedUserPlant.getPlantTypeId() != null) {
            userPlant.setPlantTypeId(updatedUserPlant.getPlantTypeId());
        }
        if (updatedUserPlant.getMaterialId() != null) {
            userPlant.setMaterialId(updatedUserPlant.getMaterialId());
        }
        if (updatedUserPlant.getHigh() != null) {
            userPlant.setHigh(updatedUserPlant.getHigh());
        }
        if (updatedUserPlant.getPotSize() != null) {
            userPlant.setPotSize(updatedUserPlant.getPotSize());
        }
        if (updatedUserPlant.getSoilLoosenerk() != null) {
            userPlant.setSoilLoosenerk(updatedUserPlant.getSoilLoosenerk());
        }

        return userPlantRepository.save(userPlant);
    }

    /**
     * Удалить растение.
     *
     * @param id уникальный идентификатор растения
     * @throws ResponseStatusException если растение не найдено
     */
    public UserPlant deleteUserPlant(Integer id) {
        UserPlant userPlant = userPlantRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не существует растения с таким id."));

        userPlantRepository.delete(userPlant);
        return userPlant;
    }
}
