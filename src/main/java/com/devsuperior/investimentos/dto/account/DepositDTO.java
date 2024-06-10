package com.devsuperior.investimentos.dto.account;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DepositDTO {

    @NotNull(message = "campo requerido!")
    @Positive(message = "Impossível Depositar Valores Negativos!")
    private Double amount;
    private String password;

    public DepositDTO() {
    }

    public DepositDTO(Double amount, String password) {
        this.amount = amount;
        this.password = password;
    }

    public DepositDTO(Double amount) {
        this.amount = amount;
    }

    public Double getAmount() {
        return amount;
    }

    public String getPassword() {
        return password;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
