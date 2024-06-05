package com.devsuperior.investimentos.controllers;

import com.devsuperior.investimentos.dto.portfolio.PortfolioDTO;
import com.devsuperior.investimentos.dto.portfolio.StockComparisonDTO;
import com.devsuperior.investimentos.dto.portfolio.StockPurchasedDTO;
import com.devsuperior.investimentos.dto.stockPortfolio.StockPortfolioDTO;
import com.devsuperior.investimentos.dto.stockPortfolio.StockPortfolioSaleDTO;
import com.devsuperior.investimentos.dto.stockPortfolio.StockPortfolioSaleResponseDTO;
import com.devsuperior.investimentos.dto.stockPortfolio.StockPurchaseDTO;
import com.devsuperior.investimentos.entities.PortfolioStock;
import com.devsuperior.investimentos.services.PortfolioService;
import com.devsuperior.investimentos.services.PortfolioStockService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/portfolios")
public class PortfolioController {

    @Autowired
    private PortfolioService service;

    @Autowired
    private PortfolioStockService portfolioStockService;

    @PreAuthorize("hasAnyRole('ROLE_CLIENT')")
    @PostMapping
    public ResponseEntity<PortfolioDTO> insert(@RequestBody @Valid PortfolioDTO dto){
        dto = service.insert(dto);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<PortfolioDTO> findById(@PathVariable Long id){
        PortfolioDTO response = service.findById(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT')")
    @GetMapping
    public ResponseEntity<List<PortfolioDTO>> findAll() {
        List<PortfolioDTO> result = service.findAll();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{id}/purchased")
    public ResponseEntity<StockPurchasedDTO> purchasedStock(@PathVariable(name = "id") Long id, @RequestBody StockPurchaseDTO dto){
        StockPurchasedDTO response = service.purchasedStock(id, dto);
        return ResponseEntity.ok(response);//Retornar um DTO com todas as ações daquele portfolio
    }

    @GetMapping("/{id}/comparison")
    public ResponseEntity<List<StockComparisonDTO>> comparison(@PathVariable(name = "id") Long id){
        List<StockComparisonDTO> response = service.comparison(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/{stockId}")
    public ResponseEntity<StockPortfolioDTO> findByStockIdAndPortfolioId(@PathVariable(name = "id") Long id, @PathVariable(name = "stockId") String stockId){
        StockPortfolioDTO response = portfolioStockService.searchByIds(stockId, id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/{stockId}/sale")
    public ResponseEntity<StockPortfolioSaleResponseDTO> sale(@PathVariable(name = "id") Long id,
                                                                  @PathVariable(name = "stockId") String stockId,
                                                                  @Valid @RequestBody StockPortfolioSaleDTO dto)
    {
        StockPortfolioSaleResponseDTO response = portfolioStockService.sale(id, stockId, dto);
        return ResponseEntity.ok(response);
    }

}
