package com.devsuperior.investimentos.testes;

import com.devsuperior.investimentos.projection.PortfolioStockProjection;

public class PortfolioStockImpl implements PortfolioStockProjection {

    private Double price;
    private Integer quantity;
    private Double valuePurchased;
    private String stockId;

    public PortfolioStockImpl(Double price, Integer quantity, Double valuePurchased, String stockId) {
        this.price = price;
        this.quantity = quantity;
        this.valuePurchased = valuePurchased;
        this.stockId = stockId;
    }

    @Override
    public Double getPrice() {
        return price;
    }

    @Override
    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public Double getValuePurchased() {
        return valuePurchased;
    }

    @Override
    public String getStockId() {
        return stockId;
    }
}
