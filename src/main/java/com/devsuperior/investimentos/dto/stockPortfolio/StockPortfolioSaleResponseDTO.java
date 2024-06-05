package com.devsuperior.investimentos.dto.stockPortfolio;

import com.devsuperior.investimentos.entities.PortfolioStock;

public class StockPortfolioSaleResponseDTO {
    private Double priceSale;
    private Double pricePurchased;
    private Integer quantitySale;
    private Double saleValue;

    public StockPortfolioSaleResponseDTO() {
    }

    public StockPortfolioSaleResponseDTO(Double priceSale, Double pricePurchased, Double saleValue) {
        this.priceSale = priceSale;
        this.pricePurchased = pricePurchased;
        this.saleValue = saleValue;
    }

    public Double getPriceSale() {
        return priceSale;
    }

    public void setPriceSale(Double priceSale) {
        this.priceSale = priceSale;
    }

    public Double getPricePurchased() {
        return pricePurchased;
    }

    public void setPricePurchased(Double pricePurchased) {
        this.pricePurchased = pricePurchased;
    }

    public Integer getQuantitySale() {
        return quantitySale;
    }

    public void setQuantitySale(Integer quantitySale) {
        this.quantitySale = quantitySale;
    }

    public Double getSaleValue() {
        return saleValue;
    }

    public void setSaleValue(Double saleValue) {
        this.saleValue = saleValue;
    }
}
