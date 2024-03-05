package com.devsuperior.investimentos.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class UserDTO {

    private Long id;

    @NotBlank(message = "Campo Requerido!")
    private String firstName;

    @NotBlank(message = "Campo Requerido!")
    private String lastName;
    private String birthDate;

    @Email(message = "email no formato inválido!")
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
