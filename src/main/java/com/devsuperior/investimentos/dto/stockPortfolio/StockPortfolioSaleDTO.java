package com.devsuperior.investimentos.dto.stockPortfolio;

import jakarta.validation.constraints.Positive;

public class StockPortfolioSaleDTO {
    @Positive(message = "Valores negativos não são permitidos")
    private Integer quantity;
    private String password;


    public StockPortfolioSaleDTO() {
    }

    public StockPortfolioSaleDTO(Integer quantity, String password) {
        this.quantity = quantity;
        this.password = password;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getPassword() {
        return password;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
