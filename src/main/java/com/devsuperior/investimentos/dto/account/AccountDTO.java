package com.devsuperior.investimentos.dto.account;

import com.devsuperior.investimentos.dto.UserDTO;
import com.devsuperior.investimentos.entities.Account;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AccountDTO {

    private Long id;
    private String name;

    @NotBlank(message = "Campo Requerido")
    @Size(min = 3, message = "Tamanho mínimo de 3 caracteres")
    private String description;
    private Double balance;
    private Integer portfolioNumber;
    private UserDTO userDTO;

    public AccountDTO() {
    }

    public AccountDTO(Long id, String name, String description, Double balance, Integer portfolioNumber) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.balance = balance;
        this.portfolioNumber = portfolioNumber;
    }

    public AccountDTO(Account entity) {
        id = entity.getId();
        name = entity.getName();
        description = entity.getDescription();
        balance = entity.getBalance();
        portfolioNumber = entity.getPortfolioNumber();
        userDTO = new UserDTO(entity.getUser());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getBalance() {
        return balance;
    }

    public Integer getPortfolioNumber() {
        return portfolioNumber;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }
}
