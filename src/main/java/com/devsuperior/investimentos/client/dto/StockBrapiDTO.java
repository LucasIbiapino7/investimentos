package com.devsuperior.investimentos.client.dto;

public class StockBrapiDTO {

    private String stock;
    private String name;

    public StockBrapiDTO() {
    }

    public StockBrapiDTO(String stock, String name) {
        this.stock = stock;
        this.name = name;
    }

    public String getStock() {
        return stock;
    }

    public String getName() {
        return name;
    }
}
