package com.devsuperior.investimentos.dto;

import com.devsuperior.investimentos.services.validation.UserInsertValid;

@UserInsertValid
public class UserInsertDTO extends UserDTO{

    private String password;

    public UserInsertDTO() {
    }

    public UserInsertDTO(Long id, String firstName, String lastName, String birthDate, String email, String password) {
        super(id, firstName, lastName, birthDate, email);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
