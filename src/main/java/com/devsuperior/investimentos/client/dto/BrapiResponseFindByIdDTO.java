package com.devsuperior.investimentos.client.dto;

import java.util.List;

public class BrapiResponseFindByIdDTO {

    private List<StockBrapiFindByIdDTO> results;

    public BrapiResponseFindByIdDTO() {
    }

    public BrapiResponseFindByIdDTO(List<StockBrapiFindByIdDTO> results, Boolean error, String message) {
        this.results = results;
    }

    public List<StockBrapiFindByIdDTO> getResults() {
        return results;
    }

}
