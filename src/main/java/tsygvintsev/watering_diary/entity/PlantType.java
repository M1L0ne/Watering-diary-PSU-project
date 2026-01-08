package tsygvintsev.watering_diary.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Plant_type")
public class PlantType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, insertable = false, updatable = false)
    private Integer id;

    @Column(name = "name", length = 60, nullable = false, unique = true)
    private String name;

    @Column(name = "description", length = 225)
    private String description;

    @Column(name = "watering_k")
    private Integer wateringK;

    public PlantType() {}

    public PlantType(String name, String description, Integer wateringK) {
        this.name = name;
        this.description = description;
        this.wateringK = wateringK;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {this.name = name;}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getWateringK() {
        return wateringK;
    }

    public void setWateringK(Integer wateringK) {
        this.wateringK = wateringK;
    }
}
