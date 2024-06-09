package com.devsuperior.investimentos.services;

import com.devsuperior.investimentos.client.dto.StockBrapiFindByIdDTO;
import com.devsuperior.investimentos.dto.stockPortfolio.StockPortfolioDTO;
import com.devsuperior.investimentos.dto.stockPortfolio.StockPortfolioSaleDTO;
import com.devsuperior.investimentos.dto.stockPortfolio.StockPortfolioSaleResponseDTO;
import com.devsuperior.investimentos.entities.PortfolioStock;
import com.devsuperior.investimentos.entities.User;
import com.devsuperior.investimentos.repositories.PortfolioStockRepository;
import com.devsuperior.investimentos.services.exceptions.AccountException;
import com.devsuperior.investimentos.services.exceptions.PasswordException;
import com.devsuperior.investimentos.services.exceptions.ResourceNotFoundException;
import com.devsuperior.investimentos.testes.Factory;
import com.devsuperior.investimentos.testes.PortfolioStockImpl;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class PortfolioStockServiceTest {

    @InjectMocks
    private PortfolioStockService service;

    @Mock
    private PortfolioStockRepository repository;

    @Mock
    private UserService userService;

    @Mock
    private StockService stockService;

    private String stockIdExisting;
    private Long portfolioIdExisting;
    private String stockIdNonExisting;
    private Long portfolioIdNonExisting;
    private PortfolioStockImpl portfolioStockImpl;
    private PortfolioStock portfolioStock;
    private StockPortfolioSaleDTO stockPortfolioSaleDTO;
    private User user;
    private StockBrapiFindByIdDTO stockBrapiFindByIdDTO;

    @BeforeEach
    void setUp() {

        stockIdExisting = "AMZO34";
        stockIdNonExisting = "";
        portfolioIdExisting = 1L;
        portfolioIdNonExisting = 100L;
        portfolioStockImpl = Factory.createPortfolioStockProjection();
        portfolioStock = Factory.createPortfolioStock();
        stockPortfolioSaleDTO = Factory.createStockPortfolioSaleDTO();
        user = Factory.createUserWithAccount();
        stockBrapiFindByIdDTO = Factory.createStockBrapiFindByIdDTO();

        Mockito.when(repository.searchByIds(stockIdExisting, portfolioIdExisting)).thenReturn(Optional.of(portfolioStockImpl));
        Mockito.when(repository.searchByIds(stockIdNonExisting, portfolioIdNonExisting)).thenReturn(Optional.empty());
        Mockito.when(repository.saveAndFlush(ArgumentMatchers.any())).thenReturn(portfolioStock);

        Mockito.when(repository.search(portfolioIdExisting, stockIdExisting)).thenReturn(Optional.of(portfolioStock));
        Mockito.when(repository.search(portfolioIdNonExisting, stockIdNonExisting)).thenReturn(Optional.empty());

        Mockito.when(stockService.findById(ArgumentMatchers.any())).thenReturn(stockBrapiFindByIdDTO);
    }

    @Test
    public void searchByIdsShouldReturnStockPortfolioDTOWhenPortfolioStockExistsInDatabase(){

        StockPortfolioDTO response = service.searchByIds(stockIdExisting, portfolioIdExisting);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getPrice(), portfolioStockImpl.getPrice());

    }

    @Test
    public void searchByIdsShouldThrowResourceNotFoundExceptionWhenPortfolioStockNonExistsInDatabase(){

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            StockPortfolioDTO response = service.searchByIds(stockIdNonExisting, portfolioIdNonExisting);
        });

    }

    @Test
    public void saleShouldPersistAndReturnStockPortfolioSaleResponseDTOWhenUserLoggedAndPortfolioStockExistsInDatabaseAndPasswordCorrectAndQuantityValid(){

        Mockito.when(userService.authenticated()).thenReturn(user);
        Mockito.when(userService.authPassword(stockPortfolioSaleDTO.getPassword(), user)).thenReturn(true);

        StockPortfolioSaleResponseDTO response = service.sale(portfolioIdExisting, stockIdExisting, stockPortfolioSaleDTO);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getQuantitySale(), stockPortfolioSaleDTO.getQuantity());

    }

    @Test
    public void saleShouldThrowResourceNotFoundExceptionWhenPortfolioStockNoExistingInDatabase(){

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            StockPortfolioSaleResponseDTO response = service.sale(portfolioIdNonExisting, stockIdNonExisting, stockPortfolioSaleDTO);
        });

    }

    @Test
    public void saleShouldThrowUsernameNotFoundExceptionWhenUserNonLogged(){

        Mockito.when(userService.authenticated()).thenThrow(UsernameNotFoundException.class);

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            StockPortfolioSaleResponseDTO response = service.sale(portfolioIdExisting, stockIdExisting, stockPortfolioSaleDTO);
        });

    }

    @Test
    public void saleShouldThrowPasswordExceptionWhenPortfolioStockExistingInDatabaseButPasswordIsIncorrect(){

        Mockito.when(userService.authenticated()).thenReturn(user);
        Mockito.when(userService.authPassword(stockPortfolioSaleDTO.getPassword(), user)).thenReturn(false);

        Assertions.assertThrows(PasswordException.class, () -> {
            StockPortfolioSaleResponseDTO response = service.sale(portfolioIdExisting, stockIdExisting, stockPortfolioSaleDTO);
        });

    }

    @Test
    public void saleShouldThrowAccountExceptionExceptionWhenPortfolioStockExistingInDatabaseButQuantityInvalid(){

        //Quantidade desse portfolio Stock = 10
        // Qyantidade do DTO = 15
        stockPortfolioSaleDTO.setQuantity(15);

        Mockito.when(userService.authenticated()).thenReturn(user);
        Mockito.when(userService.authPassword(stockPortfolioSaleDTO.getPassword(), user)).thenReturn(true);

        Assertions.assertThrows(AccountException.class, () -> {
            StockPortfolioSaleResponseDTO response = service.sale(portfolioIdExisting, stockIdExisting, stockPortfolioSaleDTO);
        });

    }

}