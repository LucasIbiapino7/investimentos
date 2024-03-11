package com.devsuperior.investimentos.entities;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_portfolio")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String description;
    private Double totalValue;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @OneToMany(mappedBy = "id.portfolio")
    private Set<PortfolioStock> portfolioStocks = new HashSet<>();

    public Portfolio() {
    }

    public Portfolio(Long id, String description, Double totalValue, Account account) {
        this.id = id;
        this.description = description;
        this.totalValue = totalValue;
        this.account = account;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Set<PortfolioStock> getStocks() {
        return portfolioStocks;
    }

    public List<Stock> getStocksPortfolio(){
        return portfolioStocks.stream().map(x -> x.getStock()).toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Portfolio portfolio = (Portfolio) o;

        return Objects.equals(id, portfolio.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
