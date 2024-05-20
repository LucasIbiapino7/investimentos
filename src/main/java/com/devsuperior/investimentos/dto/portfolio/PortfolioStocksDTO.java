package com.devsuperior.investimentos.dto.portfolio;

import com.devsuperior.investimentos.entities.PortfolioStock;

public class PortfolioStocksDTO {

    private String name;
    private Integer quantity;
    private Double price;
    private Double valuePurchased;

    public PortfolioStocksDTO() {
    }

    public PortfolioStocksDTO(PortfolioStock entity) {
        name = entity.getStock().getName();
        quantity = entity.getQuantity();
        price = entity.getPrice();
        valuePurchased = entity.getValue();
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public Double getValuePurchased() {
        return valuePurchased;
    }

    public String getName() {
        return name;
    }
}
