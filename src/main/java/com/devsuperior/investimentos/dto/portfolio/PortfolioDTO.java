package com.devsuperior.investimentos.dto.portfolio;

import com.devsuperior.investimentos.dto.account.AccountDTO;
import com.devsuperior.investimentos.entities.Portfolio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PortfolioDTO {

    private Long id;

    @NotBlank(message = "campo requerido!")
    @Size(min = 5, message = "Tamanho mínimo: 5 caracteres")
    private String description;
    private Double totalValue;
    private Long accountId;

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

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
