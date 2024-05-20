package com.devsuperior.investimentos.dto.portfolio;

import com.devsuperior.investimentos.entities.Portfolio;
import com.devsuperior.investimentos.entities.PortfolioStock;

import java.util.ArrayList;
import java.util.List;

public class StockPurchasedDTO {

    private Double balance;

    private List<PortfolioStocksDTO> stocksDTOList = new ArrayList<>();

    public StockPurchasedDTO() {
    }

    public StockPurchasedDTO(Double balance, Portfolio portfolio) {
        this.balance = balance;
        for (PortfolioStock entity : portfolio.getPortfolioStocks()){
            stocksDTOList.add(new PortfolioStocksDTO(entity));
        }
    }

    public Double getBalance() {
        return balance;
    }

    public List<PortfolioStocksDTO> getStocksDTOList() {
        return stocksDTOList;
    }

    public Double getTotal(){
        double sum = 0;
        for (PortfolioStocksDTO entity : stocksDTOList){
            sum += entity.getValuePurchased();
        }
        return sum;
    }
}
