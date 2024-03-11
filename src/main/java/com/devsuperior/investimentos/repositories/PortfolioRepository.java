package com.devsuperior.investimentos.repositories;

import com.devsuperior.investimentos.entities.Account;
import com.devsuperior.investimentos.entities.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
}
