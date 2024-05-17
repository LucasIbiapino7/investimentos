package com.devsuperior.investimentos.client.dto;

import java.util.List;

public class BrapiResponseListDTO {

    private List<StockBrapiDTO> stocks;

    public BrapiResponseListDTO() {
    }

    public BrapiResponseListDTO(List<StockBrapiDTO> stocks) {
        this.stocks = stocks;
    }

    public List<StockBrapiDTO> getStocks() {
        return stocks;
    }
}
