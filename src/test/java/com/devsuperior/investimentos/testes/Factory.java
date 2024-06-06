package com.devsuperior.investimentos.testes;

import com.devsuperior.investimentos.client.dto.StockBrapiFindByIdDTO;
import com.devsuperior.investimentos.entities.Stock;

public class Factory {

    public static StockBrapiFindByIdDTO createStockBrapiFindByIdDTO(){
        StockBrapiFindByIdDTO stockBrapiFindByIdDTO = new StockBrapiFindByIdDTO("AMZNO34", "Amazon.inc", 40.0);
        return stockBrapiFindByIdDTO;
    }

    public static Stock createStock(){
        Stock stock = new Stock("AMZNO34", "Amazon.inc");
        return stock;
    }
}
