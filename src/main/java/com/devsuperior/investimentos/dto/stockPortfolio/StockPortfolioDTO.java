package com.devsuperior.investimentos.dto.stockPortfolio;

import com.devsuperior.investimentos.entities.PortfolioStock;
import com.devsuperior.investimentos.projection.PortfolioStockProjection;

public class StockPortfolioDTO {

    private String symbol;
    private Integer quantity;
    private Double price;
    private Double valuePurchased;

    public StockPortfolioDTO() {
    }

    public StockPortfolioDTO(PortfolioStock entity) {
        symbol = entity.getStock().getName();
        quantity = entity.getQuantity();
        price = entity.getPrice();
        valuePurchased = entity.getValue();
    }

    public StockPortfolioDTO(PortfolioStockProjection projection) {
        symbol = projection.getStockId();
        quantity = projection.getQuantity();
        price = projection.getPrice();
        valuePurchased = projection.getValuePurchased();
    }

    public String getSymbol() {
        return symbol;
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
}
