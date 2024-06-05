package com.devsuperior.investimentos.repositories;

import com.devsuperior.investimentos.entities.PortfolioStock;
import com.devsuperior.investimentos.entities.PortfolioStockPK;
import com.devsuperior.investimentos.projection.PortfolioStockProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PortfolioStockRepository extends JpaRepository<PortfolioStock, PortfolioStockPK> {

    @Query(nativeQuery = true, value = """
            SELECT price, quantity, value_purchased AS valuePurchased, stock_id AS stockId
            FROM tb_portfolio_stock
            INNER JOIN tb_stock ON tb_stock.name = tb_portfolio_stock.stock_id
            WHERE tb_stock.name = :stockId AND tb_portfolio_stock.portfolio_id = :portfolioId
            """)
    Optional<PortfolioStockProjection> searchByIds(String stockId, Long portfolioId);

    @Query(nativeQuery = true, value = """
            SELECT *
            FROM tb_portfolio_stock
            INNER JOIN tb_stock ON tb_stock.name = tb_portfolio_stock.stock_id
            WHERE tb_stock.name = :stockId AND tb_portfolio_stock.portfolio_id = :id
            """)
    Optional<PortfolioStock> search(Long id, String stockId);
}
