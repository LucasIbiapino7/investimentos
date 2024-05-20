package com.devsuperior.investimentos.repositories;

import com.devsuperior.investimentos.entities.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, String > {
}
