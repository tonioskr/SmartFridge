package com.example.SmartFridge.DTO;
import com.example.SmartFridge.model.SimpleLocalDateProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;

import static java.time.LocalDate.now;

public class userDTO {
    private SimpleStringProperty id = new SimpleStringProperty();
    private SimpleStringProperty username = new SimpleStringProperty();
    private SimpleStringProperty password = new SimpleStringProperty();
    private SimpleStringProperty name = new SimpleStringProperty();
    private SimpleStringProperty surname = new SimpleStringProperty();
    private SimpleLocalDateProperty registrationDate = new SimpleLocalDateProperty(now());
    private SimpleStringProperty country = new SimpleStringProperty();

    public userDTO(String id,String u,String p, String n,String sn,
                   LocalDate r, String c) {
        this.id.set(id);
        username.set(u);
        password.set(p);
        name.set(n);
        surname.set(sn);
        registrationDate.set(r);
        country.set(c);
    }

    public userDTO(String id, String country,String username) {
        this.id.set(id);
        this.username.set(username);
        this.country.set(country);
    }

    public userDTO() {}

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getUsername() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public String getPassword() {
        return password.get();
    }

    public SimpleStringProperty passwordProperty() {
        return password;
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSurname() {
        return surname.get();
    }

    public SimpleStringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public SimpleLocalDateProperty getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(SimpleLocalDateProperty registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getCountry() {
        return country.get();
    }

    public SimpleStringProperty countryProperty() {
        return country;
    }

    public void setCountry(String country) {
        this.country.set(country);
    }
}

