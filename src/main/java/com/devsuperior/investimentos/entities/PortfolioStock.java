package com.devsuperior.investimentos.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_portfolio_stock")
public class PortfolioStock {

    @EmbeddedId
    private PortfolioStockPK id = new PortfolioStockPK();

    private Integer quantity;
    private Double price;
    private Double valuePurchased;


    public PortfolioStock() {
    }

    public PortfolioStock(Portfolio portfolio, Stock stock , Integer quantity, Double price, Double valuePurchased) {
        id.setPortfolio(portfolio);
        id.setStock(stock);
        this.quantity = quantity;
        this.price = price;
        this.valuePurchased = valuePurchased;
    }

    public Portfolio getPortfolio(){
        return id.getPortfolio();
    }

    public void setPortfolio(Portfolio portfolio){
        id.setPortfolio(portfolio);
    }

    public Stock getStock(){
        return id.getStock();
    }

    public void setStock(Stock stock){
        id.setStock(stock);
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getValue() {
        return valuePurchased;
    }

    public void setValue(Double value) {
        this.valuePurchased = value;
    }

    @Override
    public String toString() {
        return "PortfolioStock{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", price=" + price +
                ", valuePurchased=" + valuePurchased +
                '}';
    }
}
