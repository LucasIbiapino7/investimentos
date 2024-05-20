package com.devsuperior.investimentos.repositories;

import com.devsuperior.investimentos.entities.PortfolioStock;
import com.devsuperior.investimentos.entities.PortfolioStockPK;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioStockRepository extends JpaRepository<PortfolioStock, PortfolioStockPK> {
}
