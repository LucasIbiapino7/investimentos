package com.devsuperior.investimentos.services;

import com.devsuperior.investimentos.client.dto.StockBrapiFindByIdDTO;
import com.devsuperior.investimentos.dto.portfolio.PortfolioDTO;
import com.devsuperior.investimentos.dto.portfolio.StockComparisonDTO;
import com.devsuperior.investimentos.dto.portfolio.StockPurchasedDTO;
import com.devsuperior.investimentos.dto.stockPortfolio.StockPurchaseDTO;
import com.devsuperior.investimentos.entities.Account;
import com.devsuperior.investimentos.entities.Portfolio;
import com.devsuperior.investimentos.entities.PortfolioStock;
import com.devsuperior.investimentos.entities.Stock;
import com.devsuperior.investimentos.repositories.PortfolioRepository;
import com.devsuperior.investimentos.repositories.PortfolioStockRepository;
import com.devsuperior.investimentos.services.exceptions.AccountException;
import com.devsuperior.investimentos.services.exceptions.ResourceNotFoundException;
import com.devsuperior.investimentos.testes.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class PortfolioServiceTest {

    @InjectMocks
    private PortfolioService service;

    @Mock
    private PortfolioRepository repository;

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @Mock
    private StockService stockService;

    @Mock
    private PortfolioStockRepository portfolioStockRepository;

    private Long idExists;
    private Long idNonExists;
    private PortfolioDTO portfolioDTO;
    private Account account;
    private Portfolio portfolio;
    private StockPurchaseDTO stockPurchaseDTO;
    private Stock stock;
    private PortfolioStock portfolioStock;
    private StockBrapiFindByIdDTO stockBrapiFindByIdDTO;

    @BeforeEach
    void setUp() {

        idExists = 1L;
        idNonExists = 100L;
        portfolioDTO = Factory.createPortfolioDTO();
        account = Factory.createAccountWithId();
        portfolio = Factory.createPortfolio();
        stockPurchaseDTO = Factory.createStockPurchaseDTO();
        stock = Factory.createStock();
        portfolioStock = Factory.createPortfolioStock();
        stockBrapiFindByIdDTO = Factory.createStockBrapiFindByIdDTO();

        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(portfolio);

        Mockito.when(repository.findById(idExists)).thenReturn(Optional.of(portfolio));
        Mockito.when(repository.findById(idNonExists)).thenReturn(Optional.empty());

        Mockito.when(stockService.getStock(ArgumentMatchers.any(), ArgumentMatchers.any())).thenReturn(stock);
        Mockito.when(stockService.findById(ArgumentMatchers.any())).thenReturn(stockBrapiFindByIdDTO);

        Mockito.when(portfolioStockRepository.saveAndFlush(ArgumentMatchers.any())).thenReturn(portfolioStock);

        Mockito.when(repository.getReferenceById(idExists)).thenReturn(portfolio);

    }

    @Test
    public void insertShouldPersistAndReturnPortfolioDTOWhenUserLoggedWithAccount(){

        Mockito.when(userService.getAccount()).thenReturn(account);

        PortfolioDTO response = service.insert(portfolioDTO);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getId(), portfolio.getId());
        Assertions.assertEquals(response.getAccountId(), account.getId());

    }

    @Test
    public void insertShouldUsernameNotFoundExceptionWhenUserIsNotLogged(){

        Mockito.when(userService.getAccount()).thenThrow(UsernameNotFoundException.class);

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            PortfolioDTO response = service.insert(portfolioDTO);
        });

    }

    @Test
    public void insertShouldThrowResourceNotFoundExceptionWhenUserIsLoggedAndNoAccount(){

        Mockito.when(userService.getAccount()).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            PortfolioDTO response = service.insert(portfolioDTO);
        });

    }

    @Test
    public void findAllShouldReturnListPortfolioDTOWhenUserLoggedWithAccount(){

        account.getPortfolios().add(Factory.createPortfolio());

        Mockito.when(userService.getAccount()).thenReturn(account);

        List<PortfolioDTO> response = service.findAll();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.get(0).getAccountId(), account.getId());
    }

    @Test
    public void findAllShouldUsernameNotFoundExceptionWhenUserIsNotLogged(){

        Mockito.when(userService.getAccount()).thenThrow(UsernameNotFoundException.class);

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            List<PortfolioDTO> response = service.findAll();
        });

    }

    @Test
    public void findAllShouldThrowResourceNotFoundExceptionWhenUserIsLoggedAndNoAccount(){

        Mockito.when(userService.getAccount()).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            List<PortfolioDTO> response = service.findAll();
        });

    }


    @Test
    public void findByIdShouldReturnPortfolioDTOWhenIdExistsAndPortfolioIsSelf(){

        Mockito.when(authService.AuthPortfolioIsSelf(portfolio)).thenReturn(true);

        PortfolioDTO response = service.findById(idExists);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getId(), portfolio.getId());

    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdNonExists(){

        Mockito.when(authService.AuthPortfolioIsSelf(portfolio)).thenReturn(true);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            PortfolioDTO response = service.findById(idNonExists);
        });

    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdExistsButPortfolioNotIsSelf(){

        Mockito.when(authService.AuthPortfolioIsSelf(portfolio)).thenReturn(false);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            PortfolioDTO response = service.findById(idExists);
        });

    }

    @Test
    public void purchasedStockShouldPersistAndReturnStockPurchasedDTOWhenIdExistsAndPortfolioIsSelfAndValueIsValid(){

        Mockito.when(authService.AuthPortfolioIsSelf(portfolio)).thenReturn(true);

        //Balance da conta desse Portfolio é 1000.0
        double value = portfolio.getAccount().getBalance() - stockPurchaseDTO.getRegularMarketPlace() * stockPurchaseDTO.getQuantity();

        StockPurchasedDTO response = service.purchasedStock(idExists, stockPurchaseDTO);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getBalance(), value);

    }

    @Test
    public void purchasedStockShouldThrowResourceNotFoundExceptionWhenIdNonExists(){

        Mockito.when(authService.AuthPortfolioIsSelf(portfolio)).thenReturn(true);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            StockPurchasedDTO response = service.purchasedStock(idNonExists, stockPurchaseDTO);
        });

    }

    @Test
    public void purchasedStockShouldThrowResourceNotFoundExceptionWhenIdExistsButPortfolioNotIsSelf(){

        Mockito.when(authService.AuthPortfolioIsSelf(portfolio)).thenReturn(false);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            StockPurchasedDTO response = service.purchasedStock(idExists, stockPurchaseDTO);
        });

    }

    @Test
    public void purchasedStockShouldThrowAccountExceptionWhenIdExistsAndPortfolioIsSelfButValueIsInvalid(){

        Mockito.when(authService.AuthPortfolioIsSelf(portfolio)).thenReturn(true);

        //Balance da conta desse Portfolio é 0.0
        portfolio.getAccount().setBalance(0.0);

        Assertions.assertThrows(AccountException.class, () -> {
            StockPurchasedDTO response = service.purchasedStock(idExists, stockPurchaseDTO);
        });

    }

    @Test
    public void comparisonReturnListOfStockComparisonDTOWhenIdPortfolioExists(){

        List<StockComparisonDTO> response = service.comparison(idExists);

        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.isEmpty());

    }

}