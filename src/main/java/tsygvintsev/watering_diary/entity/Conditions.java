package tsygvintsev.watering_diary.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Conditions")
public class Conditions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, insertable = false, updatable = false)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "temperature", nullable = false)
    private Integer temperature;

    @Column(name = "watering", nullable = false)
    private Integer watering;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User user;

    public Conditions() {}

    public Conditions(Integer userId, LocalDate date, Integer temperature, Integer watering) {
        this.userId = userId;
        this.date = date;
        this.temperature = temperature;
        this.watering = watering;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public Integer getWatering() {
        return watering;
    }

    public void setWatering(Integer watering) {
        this.watering = watering;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
