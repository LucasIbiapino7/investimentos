package com.devsuperior.investimentos.dto.portfolio;

import com.devsuperior.investimentos.dto.account.AccountDTO;
import com.devsuperior.investimentos.dto.stockPortfolio.StockPortfolioDTO;
import com.devsuperior.investimentos.entities.Portfolio;
import com.devsuperior.investimentos.entities.PortfolioStock;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PortfolioDTO {

    private Long id;

    @NotBlank(message = "campo requerido!")
    @Size(min = 5, message = "Tamanho mínimo: 5 caracteres")
    private String description;
    private Double totalValue;
    private Long accountId;
    private List<StockPortfolioDTO> stockPortfolios = new ArrayList<>();

    public PortfolioDTO() {
    }

    public PortfolioDTO(Long id, String description, Double totalValue, Long accountId) {
        this.id = id;
        this.description = description;
        this.totalValue = totalValue;
        this.accountId = accountId;
    }

    public PortfolioDTO(Portfolio entity) {
        id = entity.getId();
        description = entity.getDescription();
        totalValue = entity.getTotalValue();
        accountId = entity.getAccount().getId();
        for (PortfolioStock obj : entity.getPortfolioStocks()){
            this.stockPortfolios.add(new StockPortfolioDTO(obj));
        }
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

    public List<StockPortfolioDTO> getStockPortfolios() {
        return stockPortfolios;
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

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
