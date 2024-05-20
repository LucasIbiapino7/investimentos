package com.devsuperior.investimentos.services;

import com.devsuperior.investimentos.dto.portfolio.PortfolioDTO;
import com.devsuperior.investimentos.dto.portfolio.StockPurchasedDTO;
import com.devsuperior.investimentos.dto.stockPortfolio.StockPurchaseDTO;
import com.devsuperior.investimentos.entities.*;
import com.devsuperior.investimentos.repositories.PortfolioRepository;
import com.devsuperior.investimentos.repositories.PortfolioStockRepository;
import com.devsuperior.investimentos.services.exceptions.AccountException;
import com.devsuperior.investimentos.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        Portfolio entity = repository.getReferenceById(id);
        if (!authService.AuthPortfolioIsSelf(entity)){
            throw new ResourceNotFoundException("só pra testar, acesso negado");
        }
        return new PortfolioDTO(entity);
    }

    @Transactional
    public StockPurchasedDTO purchasedStock(Long id, StockPurchaseDTO dto){
        Portfolio portfolio = repository.getReferenceById(id);
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
}
