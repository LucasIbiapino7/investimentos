package com.devsuperior.investimentos.services;

import com.devsuperior.investimentos.entities.Account;
import com.devsuperior.investimentos.entities.Portfolio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    public Boolean AuthPortfolioIsSelf(Portfolio portfolio){
        Account account = userService.getAccount();
        return Objects.equals(account.getId(), portfolio.getAccount().getId());
    }

}
