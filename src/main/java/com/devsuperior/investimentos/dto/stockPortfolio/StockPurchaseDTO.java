package com.devsuperior.investimentos.dto.stockPortfolio;

public class StockPurchaseDTO {

    private String stockId;
    private String longName;
    private Double regularMarketPlace;
    private Integer quantity;

    public StockPurchaseDTO() {
    }

    public StockPurchaseDTO(String stockId, String longName, Double regularMarketPlace, Integer quantity) {
        this.stockId = stockId;
        this.longName = longName;
        this.regularMarketPlace = regularMarketPlace;
        this.quantity = quantity;
    }

    public String getStockId() {
        return stockId;
    }

    public String getLongName() {
        return longName;
    }

    public Double getRegularMarketPlace() {
        return regularMarketPlace;
    }

    public Integer getQuantity() {
        return quantity;
    }

}
