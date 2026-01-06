package tsygvintsev.watering_diary.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tsygvintsev.watering_diary.entity.Conditions;
import tsygvintsev.watering_diary.repository.ConditionsRepository;
import tsygvintsev.watering_diary.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class ConditionsService {

    @Autowired
    private ConditionsRepository conditionsRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Conditions> getAllConditions() {
        return conditionsRepository.findAll();
    }

    public Conditions getConditionsById(Integer id) {
        return conditionsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не существует записи с таким id."));
    }

    public List<Conditions> getConditionsByUserId(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Не существует пользователя с таким id.");
        }
        return conditionsRepository.findByUserId(userId);
    }

    public Conditions createConditions(Conditions conditions) {
        if (conditions.getUserId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "ID пользователя не может быть пустым.");
        }

        if (conditions.getDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Дата не может быть пустой.");
        }

        if (conditions.getDate().isBefore(LocalDate.now().minusDays(7))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Данные устарели, дата старше 7 дней.");
        }

        if (conditions.getTemperature() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Температура не может быть пустой.");
        }

        if (conditions.getWatering() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Значение полива не может быть пустым.");
        }

        if (!userRepository.existsById(conditions.getUserId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Не существует пользователя с таким id.");
        }

        if (conditionsRepository.existsByUserIdAndDate(conditions.getUserId(), conditions.getDate())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "У этого пользователя уже есть запись на эту дату.");
        }

        return conditionsRepository.save(conditions);
    }

    public Conditions updateConditions(Integer id, Conditions updatedConditions) {
        Conditions conditions = conditionsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не существует записи с таким id."
                ));

        LocalDate newDate = updatedConditions.getDate() != null ?
                updatedConditions.getDate() : conditions.getDate();
        Integer newTemperature = updatedConditions.getTemperature() != null ?
                updatedConditions.getTemperature() : conditions.getTemperature();
        Integer newWatering = updatedConditions.getWatering() != null ?
                updatedConditions.getWatering() : conditions.getWatering();

        boolean valuesChanged = !newDate.equals(conditions.getDate()) ||
                !newTemperature.equals(conditions.getTemperature()) ||
                !newWatering.equals(conditions.getWatering());

        if (updatedConditions.getDate() != null && !updatedConditions.getDate().equals(conditions.getDate())) {
            if (conditionsRepository.existsByUserIdAndDate(conditions.getUserId(), updatedConditions.getDate())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "У этого пользователя уже есть запись на эту дату.");
            }
        }

        if (updatedConditions.getDate() != null) {
            conditions.setDate(updatedConditions.getDate());
        }
        if (updatedConditions.getTemperature() != null) {
            conditions.setTemperature(updatedConditions.getTemperature());
        }
        if (updatedConditions.getWatering() != null) {
            conditions.setWatering(updatedConditions.getWatering());
        }

        return conditionsRepository.save(conditions);
    }

    public Conditions deleteConditions(Integer id) {
        Conditions conditions = conditionsRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Не существует записи с таким id."));

        conditionsRepository.delete(conditions);
        return conditions;
    }
}
