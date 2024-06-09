package com.devsuperior.investimentos.dto;

import com.devsuperior.investimentos.entities.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserDTO {

    private Long id;

    @NotBlank(message = "Campo Requerido!")
    private String firstName;

    @NotBlank(message = "Campo Requerido!")
    private String lastName;
    private String birthDate;

    @Email(message = "email no formato inválido!")
    private String email;

    public UserDTO() {
    }

    public UserDTO(Long id, String firstName, String lastName, String  birthDate, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.email = email;
    }

    public UserDTO(User entity) {
        id = entity.getId();
        firstName = entity.getFirstName();
        lastName = entity.getLastName();
        birthDate = formatDate(entity.getBirthDate());
        email = entity.getEmail();
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

    private String formatDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
}
