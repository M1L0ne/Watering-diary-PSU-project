package tsygvintsev.watering_diary.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Watering_record")
public class WateringRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, insertable = false, updatable = false)
    private Integer id;

    @Column(name = "user_plant_id")
    private Integer userPlantId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "time", nullable = false)
    private LocalTime time;

    @Column(name = "volume_watering", nullable = false)
    private Integer volumeWatering;

    @Column(name = "error_rate_k", nullable = false)
    private Integer errorRateK;

    @ManyToOne
    @JoinColumn(name = "user_plant_id", insertable = false, updatable = false)
    private UserPlant userPlant;

    public WateringRecord() {}

    public WateringRecord(Integer userPlantId, LocalDate date, LocalTime time,
                          Integer volumeWatering, Integer errorRateK) {
        this.userPlantId = userPlantId;
        this.date = date;
        this.time = time;
        this.volumeWatering = volumeWatering;
        this.errorRateK = errorRateK;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserPlantId() {
        return userPlantId;
    }

    public void setUserPlantId(Integer userPlantId) {
        this.userPlantId = userPlantId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Integer getVolumeWatering() {
        return volumeWatering;
    }

    public void setVolumeWatering(Integer volumeWatering) {
        this.volumeWatering = volumeWatering;
    }

    public Integer getErrorRateK() {
        return errorRateK;
    }

    public void setErrorRateK(Integer errorRateK) {
        this.errorRateK = errorRateK;
    }

    public UserPlant getUserPlant() {
        return userPlant;
    }

    public void setUserPlant(UserPlant userPlant) {
        this.userPlant = userPlant;
    }
}
