package tsygvintsev.watering_diary.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "User")
public class User {
    @Id
    @Column(name = "id", unique = true, insertable = false, updatable = false)
    private Integer id;

    @Column(name = "login", length = 20, nullable = false, unique = true)
    private String login;

    @Column(name = "password", length = 16, nullable = false)
    private String password;

    @Column(name = "surname", length = 30)
    private String surname;

    @Column(name = "name", length = 30)
    private String name;

    @Column(name = "patronymic", length = 30)
    private String patronymic;

    public User() {}

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }
}
