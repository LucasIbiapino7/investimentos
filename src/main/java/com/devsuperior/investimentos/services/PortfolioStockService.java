package com.devsuperior.investimentos.services;

import com.devsuperior.investimentos.client.dto.StockBrapiFindByIdDTO;
import com.devsuperior.investimentos.dto.stockPortfolio.StockPortfolioDTO;
import com.devsuperior.investimentos.dto.stockPortfolio.StockPortfolioSaleDTO;
import com.devsuperior.investimentos.dto.stockPortfolio.StockPortfolioSaleResponseDTO;
import com.devsuperior.investimentos.entities.*;
import com.devsuperior.investimentos.projection.PortfolioStockProjection;
import com.devsuperior.investimentos.repositories.PortfolioRepository;
import com.devsuperior.investimentos.repositories.PortfolioStockRepository;
import com.devsuperior.investimentos.repositories.StockRepository;
import com.devsuperior.investimentos.services.exceptions.AccountException;
import com.devsuperior.investimentos.services.exceptions.PasswordException;
import com.devsuperior.investimentos.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PortfolioStockService {

    @Autowired
    private PortfolioStockRepository repository;

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private StockService stockService;

    @Transactional(readOnly = true)
    public StockPortfolioDTO searchByIds(String stockId, Long id){
        Optional<PortfolioStockProjection> response = repository.searchByIds(stockId, id);
        if (response.isEmpty()){
            throw new ResourceNotFoundException("Não encontrado");
        }
        return new StockPortfolioDTO(response.get());
    }

    @Transactional
    public StockPortfolioSaleResponseDTO sale(Long id, String stockId, StockPortfolioSaleDTO dto) {
        //Verifica se o portfolio existe
        Optional<PortfolioStock> response = repository.search(id, stockId);
        if (response.isEmpty()){
            throw new ResourceNotFoundException("Não encontrado");
        }

        //Verifica se a senha informada é válida
        User user = userService.authenticated();
        if (!userService.authPassword(dto.getPassword(), user)){
            throw new PasswordException("Senha Inválida");
        }

        // Verifica se a quantidade é válida
        PortfolioStock portfolioStock = response.get();
        if (portfolioStock.getQuantity() < dto.getQuantity()){
            throw new AccountException("Quantidade inválida");
        }

        StockPortfolioSaleResponseDTO stockPortfolioSaleResponseDTO = new StockPortfolioSaleResponseDTO();

        //Recupera o Valor atual
        StockBrapiFindByIdDTO stock = stockService.findById(stockId);

        // Calcula o valor da venda
        Double value = dto.getQuantity() * stock.getRegularMarketPrice();

        StockPortfolioSaleResponseDTO stockPortfolioResponse = stockPortfolioFactory(stock.getRegularMarketPrice(),
                dto.getQuantity(), portfolioStock.getPrice(), value);

        //Atualiza o valor da qauntidade
        portfolioStock.setQuantity(portfolioStock.getQuantity() - dto.getQuantity());

        //Atualiza o Valor total de ações
        portfolioStock.setValue(portfolioStock.getPrice() * portfolioStock.getQuantity());

        // Salva o valor atualizado
        portfolioStock = repository.saveAndFlush(portfolioStock);

        // Adicionando o Valor ao Saldo da conta
        user.getAccount().deposit(value);

        return stockPortfolioResponse;
    }

    private StockPortfolioSaleResponseDTO stockPortfolioFactory(Double priceSale , Integer quantitySale,
                                                                Double pricePurchased,  Double saleValue) {
        StockPortfolioSaleResponseDTO dto = new StockPortfolioSaleResponseDTO();
        dto.setPriceSale(priceSale);
        dto.setQuantitySale(quantitySale);
        dto.setPricePurchased(pricePurchased);
        dto.setSaleValue(saleValue);
        return dto;
    }
}
