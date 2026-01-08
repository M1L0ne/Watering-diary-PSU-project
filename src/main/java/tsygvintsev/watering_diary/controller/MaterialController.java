package tsygvintsev.watering_diary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tsygvintsev.watering_diary.entity.Material;
import tsygvintsev.watering_diary.service.MaterialService;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Контроллер для управления материалами горшков.
 * Материалы влияют на скорость испарения влаги.
 */
@RestController
@RequestMapping("/api/materials")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    /**
     * Получить все материалы, отсортированные по коэффициенту полива.
     *
     * @return ResponseEntity со списком материалов и статусом 200 OK
     */
    @GetMapping
    public ResponseEntity<List<Material>> getAllMaterials() {
        return new ResponseEntity<>(materialService.getAllMaterials(), HttpStatus.OK);
    }

    /**
     * Получить материал по ID.
     *
     * @param id уникальный идентификатор материала
     * @return ResponseEntity с данными материала и статусом 200 OK
     * @throws ResponseStatusException если материал не найден (404 NOT_FOUND)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Material> getMaterialById(@PathVariable Integer id) {
        return new ResponseEntity<>(materialService.getMaterialById(id), HttpStatus.OK);
    }

    /**
     * Создать новый материал.
     *
     * @param material объект материала для создания
     * @return ResponseEntity с созданным материалом и статусом 201 CREATED
     * @throws ResponseStatusException если материал с таким названием уже существует
     */
    @PostMapping
    public ResponseEntity<Material> createMaterial(@RequestBody Material material) {
        return new ResponseEntity<>(materialService.createMaterial(material), HttpStatus.CREATED);
    }

    /**
     * Обновить данные материала.
     *
     * @param id уникальный идентификатор материала
     * @param material объект с обновляемыми полями
     * @return ResponseEntity с обновлённым материалом и статусом 200 OK
     * @throws ResponseStatusException если материал не найден или новое название уже занято
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Material> updateMaterial(@PathVariable Integer id,
                                                   @RequestBody Material material) {
        return new ResponseEntity<>(materialService.updateMaterial(id, material), HttpStatus.OK);
    }

    /**
     * Удалить материал.
     *
     * @param id уникальный идентификатор материала
     * @return ResponseEntity со статусом 204 NO_CONTENT
     * @throws ResponseStatusException если материал не найден
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Material> deleteMaterial(@PathVariable Integer id) {
        return new ResponseEntity<>(materialService.deleteMaterial(id), HttpStatus.NO_CONTENT);
    }
}
