package com.devsuperior.investimentos.dto.account;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class WithdrawDTO {

    @NotNull(message = "campo requerido!")
    @Positive(message = "Impossível Depositar Valores Negativos!")
    private Double amount;
    private String password;

    public WithdrawDTO() {
    }

    public WithdrawDTO(Double amount) {
        this.amount = amount;
    }

    public Double getAmount() {
        return amount;
    }

    public String getPassword() {
        return password;
    }
}
