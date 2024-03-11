package com.devsuperior.investimentos.services;

import com.devsuperior.investimentos.dto.portfolio.PortfolioDTO;
import com.devsuperior.investimentos.entities.Account;
import com.devsuperior.investimentos.entities.Portfolio;
import com.devsuperior.investimentos.entities.User;
import com.devsuperior.investimentos.repositories.PortfolioRepository;
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
    private UserService userService;

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
}
