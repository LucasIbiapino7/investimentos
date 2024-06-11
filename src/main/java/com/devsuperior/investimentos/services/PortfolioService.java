package com.devsuperior.investimentos.services;

import com.devsuperior.investimentos.client.dto.StockBrapiFindByIdDTO;
import com.devsuperior.investimentos.dto.portfolio.PortfolioDTO;
import com.devsuperior.investimentos.dto.portfolio.StockComparisonDTO;
import com.devsuperior.investimentos.dto.portfolio.StockPurchasedDTO;
import com.devsuperior.investimentos.dto.stockPortfolio.StockPurchaseDTO;
import com.devsuperior.investimentos.entities.*;
import com.devsuperior.investimentos.repositories.PortfolioRepository;
import com.devsuperior.investimentos.repositories.PortfolioStockRepository;
import com.devsuperior.investimentos.services.exceptions.AccountException;
import com.devsuperior.investimentos.services.exceptions.ForbiddenException;
import com.devsuperior.investimentos.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class PortfolioService {

    @Autowired
    private PortfolioRepository repository;

    @Autowired
    private PortfolioStockRepository portfolioStockRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Autowired
    private StockService stockService;

    @Transactional
    public PortfolioDTO insert(PortfolioDTO dto) {
        Account account = userService.getAccount();
        Portfolio portfolio = new Portfolio();
        portfolio.setDescription(dto.getDescription());
        portfolio.setTotalValue(0.0);
        portfolio.setAccount(account);
        portfolio = repository.save(portfolio);
        return new PortfolioDTO(portfolio);
    }

    @Transactional(readOnly = true)
    public List<PortfolioDTO> findAll() {
        Account account = userService.getAccount();
        List<Portfolio> portfolios = account.getPortfolios();
        return portfolios.stream().map(x -> new PortfolioDTO(x)).toList();
    }

    @Transactional(readOnly = true)
    public PortfolioDTO findById(Long id) {
        Portfolio portfolio = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Portifolio não encontrado"));
        if (!authService.AuthPortfolioIsSelf(portfolio)){
            throw new ForbiddenException("acesso negado");
        }
        return new PortfolioDTO(portfolio);
    }

    @Transactional
    public StockPurchasedDTO purchasedStock(Long id, StockPurchaseDTO dto){
        Portfolio portfolio = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Portifolio não encontrado"));
        if (!authService.AuthPortfolioIsSelf(portfolio)){
            throw new ResourceNotFoundException("só pra testar, acesso negado");
        }

        Account account = portfolio.getAccount();

        Double totalValue = dto.getRegularMarketPlace() * dto.getQuantity();

        if (totalValue > account.getBalance()){
            throw new AccountException("Saldo insuficiente para compra. Valor = " + totalValue + "Saldo = " + account.getBalance());
        }

        Stock stock = stockService.getStock(dto.getStockId(), dto.getLongName());

        PortfolioStock portfolioStock = new PortfolioStock();
        portfolioStock.setPortfolio(portfolio);
        portfolioStock.setStock(stock);
        portfolioStock.setQuantity(dto.getQuantity());
        portfolioStock.setPrice(dto.getRegularMarketPlace());
        portfolioStock.setValue(totalValue);

        portfolioStock = portfolioStockRepository.saveAndFlush(portfolioStock);

        account.withdraw(totalValue);

        return new StockPurchasedDTO(account.getBalance(), portfolio);

    }

    @Transactional(readOnly = true)
    public List<StockComparisonDTO> comparison(Long id) {
        Portfolio portfolio = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id Não existe"));

        //(lembrar de ver quantas consultas isso vai fazer)
        Set<PortfolioStock> result = portfolio.getPortfolioStocks();// Recupera os stocks de um portfólio

        return stockComparisonFactory(result);
    }

    private List<StockComparisonDTO> stockComparisonFactory(Set<PortfolioStock> result) {

        List<StockComparisonDTO> response = new ArrayList<>();


        for (PortfolioStock object : result){

            StockComparisonDTO dto = new StockComparisonDTO();

            StockBrapiFindByIdDTO stock = stockService.findById(object.getStock().getName());

            dto.setSymbol(stock.getSymbol());
            dto.setPricePurchased(object.getPrice());
            dto.setPriceActual(stock.getRegularMarketPrice());
            dto.variation();
            dto.setQuantity(object.getQuantity());
            dto.setTotalValue(object.getValue());
            dto.calcValueSale();

            response.add(dto);

        }

        return response;

    }


}
