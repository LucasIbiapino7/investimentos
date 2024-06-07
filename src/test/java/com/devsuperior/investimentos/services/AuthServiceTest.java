package com.devsuperior.investimentos.services;

import com.devsuperior.investimentos.entities.Account;
import com.devsuperior.investimentos.entities.Portfolio;
import com.devsuperior.investimentos.entities.User;
import com.devsuperior.investimentos.services.exceptions.ResourceNotFoundException;
import com.devsuperior.investimentos.testes.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService service;

    @Mock
    private UserService userService;

    private Portfolio portfolio;
    private Account accountWithId;
    private Account accountNoId;

    @BeforeEach
    void setUp() {

        portfolio = Factory.createPortfolio();
        accountWithId = Factory.createAccountWithId();
        accountNoId = Factory.createAccount();

    }

    @Test
    public void AuthPortfolioIsSelfShouldThrowResourceNotFoundExceptionWhenUserIsLoggedAndNoAccount(){

        Mockito.when(userService.getAccount()).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
           boolean response = service.AuthPortfolioIsSelf(portfolio);
        });

    }

    @Test
    public void AuthPortfolioIsSelfShouldUsernameNotFoundExceptionWhenUserIsNotLogged(){

        Mockito.when(userService.getAccount()).thenThrow(UsernameNotFoundException.class);

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            boolean response = service.AuthPortfolioIsSelf(portfolio);
        });

    }

    @Test
    public void AuthPortfolioIsSelfShouldReturnTrueWhenUserIsLoggedWithAccountAndAccountIdEqualsPortfolioId(){

        Mockito.when(userService.getAccount()).thenReturn(accountWithId);

        boolean response = service.AuthPortfolioIsSelf(portfolio);

        Assertions.assertTrue(response);

    }

    @Test
    public void AuthPortfolioIsSelfShouldReturnTrueWhenUserIsLoggedWithAccountAndAccountIdNotEqualsPortfolioId(){

        Mockito.when(userService.getAccount()).thenReturn(accountNoId);

        boolean response = service.AuthPortfolioIsSelf(portfolio);

        Assertions.assertFalse(response);

    }

}