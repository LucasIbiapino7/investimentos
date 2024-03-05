package com.devsuperior.investimentos.dto;

import java.time.LocalDate;

public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String  birthDate;
    private String email;
    private String password;

    public UserDTO() {
    }

    public UserDTO(Long id, String firstName, String lastName, String  birthDate, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
