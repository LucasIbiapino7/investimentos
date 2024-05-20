package com.devsuperior.investimentos.client.dto;

public class StockBrapiFindByIdDTO {

    private String symbol;
    private String longName;
    private Double regularMarketPrice;

    public StockBrapiFindByIdDTO() {
    }

    public StockBrapiFindByIdDTO(String symbol, String longName, Double regularMarketPrice) {
        this.symbol = symbol;
        this.longName = longName;
        this.regularMarketPrice = regularMarketPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getLongName() {
        return longName;
    }

    public Double getRegularMarketPrice() {
        return regularMarketPrice;
    }
}
