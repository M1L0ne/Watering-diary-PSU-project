package tsygvintsev.watering_diary.entity;

import jakarta.persistence.*;

/**
 * Сущность материалов горшков.
 * Влияет на скорость испарения влаги.
 */
@Entity
@Table(name = "Material")
public class Material {
    /** Уникальный идентификатор материала */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, insertable = false, updatable = false)
    private Integer id;

    /** Название материала */
    @Column(name = "name", length = 30, nullable = false, unique = true)
    private String name;

    /** Коэффициент полива (1-100) */
    @Column(name = "watering_k")
    private Integer wateringK;

    public Material() {}

    public Material(String name, Integer wateringK) {
        this.name = name;
        this.wateringK = wateringK;
    }

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public Integer getWateringK() {return wateringK;}

    public void setWateringK(Integer wateringK) {this.wateringK = wateringK;}
}
