package tsygvintsev.watering_diary.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "User_plant")
public class UserPlant {
    @Id
    @Column(name = "id", unique = true, insertable = false, updatable = false)
    private Integer id;

    @Column(name = "name", length = 30)
    private String name;

    @Column(name = "plant_type_id")
    private Integer plantTypeId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "material_id", nullable = false)
    private Integer materialId;

    @Column(name = "high")
    private Integer high;

    @Column(name = "pot_size")
    private Integer potSize;

    @Column(name = "soil_loosener_k")
    private Integer soilLoosenerk;

    @ManyToOne
    @JoinColumn(name = "Plant_type_id", insertable = false, updatable = false)
    private PlantType plantType;

    @ManyToOne
    @JoinColumn(name = "User_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "Material_id", insertable = false, updatable = false)
    private Material material;

    public UserPlant() {}

    public UserPlant(String name, Integer plantTypeId, Integer userId, Integer materialId) {
        this.name = name;
        this.plantTypeId = plantTypeId;
        this.userId = userId;
        this.materialId = materialId;
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

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPlantTypeId() {
        return plantTypeId;
    }

    public void setPlantTypeId(Integer plantTypeId) {
        this.plantTypeId = plantTypeId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public Integer getHigh() {
        return high;
    }

    public void setHigh(Integer high) {
        this.high = high;
    }

    public Integer getPotSize() {
        return potSize;
    }

    public void setPotSize(Integer potSize) {
        this.potSize = potSize;
    }

    public Integer getSoilLoosenerk() {
        return soilLoosenerk;
    }

    public void setSoilLoosenerk(Integer soilLoosenerk) {
        this.soilLoosenerk = soilLoosenerk;
    }

    public PlantType getPlantType() {
        return plantType;
    }

    public void setPlantType(PlantType plantType) {
        this.plantType = plantType;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
