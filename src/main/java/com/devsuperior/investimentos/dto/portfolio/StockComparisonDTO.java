package com.devsuperior.investimentos.dto.portfolio;

public class StockComparisonDTO {

    private String symbol;
    private Double pricePurchased;
    private Double priceActual;
    private Double variation;
    private Integer quantity;
    private Double totalValue;
    private Double totalValueSale;

    public StockComparisonDTO() {
    }

    public StockComparisonDTO(String symbol, Double pricePurchased, Double priceActual, Double variation, Integer quantity, Double totalValue, Double totalValueSale) {
        this.symbol = symbol;
        this.pricePurchased = pricePurchased;
        this.priceActual = priceActual;
        this.variation = variation;
        this.quantity = quantity;
        this.totalValue = totalValue;
        this.totalValueSale = totalValueSale;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getPricePurchased() {
        return pricePurchased;
    }

    public void setPricePurchased(Double pricePurchased) {
        this.pricePurchased = pricePurchased;
    }

    public Double getPriceActual() {
        return priceActual;
    }

    public void setPriceActual(Double priceActual) {
        this.priceActual = priceActual;
    }

    public Double getVariation() {
        return variation;
    }

    public void setVariation(Double variation) {
        this.variation = variation;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    public Double getTotalValueSale() {
        return totalValueSale;
    }

    public void setTotalValueSale(Double totalValueSale) {
        this.totalValueSale = totalValueSale;
    }

    public void variation(){
        this.variation = ((priceActual - pricePurchased) / pricePurchased) * 100;
    }

    public void calcValueSale(){
        this.totalValueSale = quantity * priceActual;
    }
}
