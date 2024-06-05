package com.devsuperior.investimentos.projection;

public interface PortfolioStockProjection {

    Double getPrice();
    Integer getQuantity();
    Double getValuePurchased();
    String getStockId();

}
