package com.devsuperior.investimentos.dto;

public class UserDeleteDTO {

    private String password;

    public UserDeleteDTO() {
    }

    public UserDeleteDTO(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}
