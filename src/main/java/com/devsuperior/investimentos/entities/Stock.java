package com.devsuperior.investimentos.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_stock")
public class Stock {

    @Id
    private String name;
    private String longName;

    @OneToMany(mappedBy = "id.stock")
    private Set<PortfolioStock> portfolioStocks = new HashSet<>();

    public Stock(String name, String longName) {
        this.name = name;
        this.longName = longName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public Set<PortfolioStock> getStocks() {
        return portfolioStocks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stock stock = (Stock) o;

        return Objects.equals(name, stock.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
