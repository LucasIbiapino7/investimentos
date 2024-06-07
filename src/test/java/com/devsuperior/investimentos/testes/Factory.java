package com.devsuperior.investimentos.testes;

import com.devsuperior.investimentos.client.dto.BrapiResponseFindByIdDTO;
import com.devsuperior.investimentos.client.dto.BrapiResponseListDTO;
import com.devsuperior.investimentos.client.dto.StockBrapiDTO;
import com.devsuperior.investimentos.client.dto.StockBrapiFindByIdDTO;
import com.devsuperior.investimentos.dto.account.AccountDTO;
import com.devsuperior.investimentos.dto.account.DepositDTO;
import com.devsuperior.investimentos.dto.account.WithdrawDTO;
import com.devsuperior.investimentos.dto.portfolio.PortfolioDTO;
import com.devsuperior.investimentos.dto.stockPortfolio.StockPurchaseDTO;
import com.devsuperior.investimentos.entities.*;

import java.time.LocalDate;
import java.util.ArrayList;

public class Factory {

    public static StockBrapiFindByIdDTO createStockBrapiFindByIdDTO(){
        StockBrapiFindByIdDTO stockBrapiFindByIdDTO = new StockBrapiFindByIdDTO("AMZNO34", "Amazon.inc", 40.0);
        return stockBrapiFindByIdDTO;
    }

    public static Stock createStock(){
        Stock stock = new Stock("AMZNO34", "Amazon.inc");
        return stock;
    }

    public static User createUser(){
        User user = new User();
        user.setId(null);
        user.setFirstName("Lucas");
        user.setLastName("Duarte");
        user.setEmail("lucas@gmail.com");
        user.setBirthDate(LocalDate.now());
        user.setPassword("123456");
        return user;
    }

    public static User createUserWithAccount(){
        User user = new User();
        user.setId(null);
        user.setFirstName("Lucas");
        user.setLastName("Duarte");
        user.setEmail("lucas@gmail.com");
        user.setPassword("123456");
        user.setBirthDate(LocalDate.now());
        user.setAccount(createAccount());
        return user;
    }

    public static Portfolio createPortfolio() {
        Portfolio portfolio = new Portfolio();
        portfolio.setId(1L);
        portfolio.setDescription("");
        portfolio.setTotalValue(0.0);
        portfolio.setAccount(createAccountWithId());
        return portfolio;
    }

    public static Account createAccountWithId(){
        Account account = new Account();
        account.setId(1L);
        account.setBalance(1000.0);
        account.setUser(createUser());
        return account;
    }

    public static Account createAccount(){
        Account account = new Account();
        account.setId(null);
        account.setBalance(1000.0);
        account.setUser(createUser());
        return account;
    }

    public static AccountDTO createAccountDTO(){
        return new AccountDTO(createAccount());
    }

    public static AccountDTO createAccountDTOWithNameNull(){
        AccountDTO accountDTO =  new AccountDTO(createAccount());
        accountDTO.setName(null);
        return accountDTO;
    }

    public static DepositDTO createDepositDTO(){
        return new DepositDTO(1000.0, "123456");
    }

    public static WithdrawDTO createWithdrawDTO(){
        return new WithdrawDTO(1000.0, "123456");
    }

    public static BrapiResponseListDTO createBrapiResponseListDTO(){
        BrapiResponseListDTO brapiResponseListDTO = new BrapiResponseListDTO(new ArrayList<>());
        brapiResponseListDTO.getStocks().add(createStockBrapiDTO());
        return brapiResponseListDTO;
    }

    public static BrapiResponseListDTO createBrapiResponseListDTOEmpty(){
        BrapiResponseListDTO brapiResponseListDTO = new BrapiResponseListDTO(new ArrayList<>());
        return brapiResponseListDTO;
    }

    public static StockBrapiDTO createStockBrapiDTO(){
        return new StockBrapiDTO("AMZO34", "Amazon.inc");
    }

    public static BrapiResponseFindByIdDTO createBrapiResponseFindByIdDTO(){
        BrapiResponseFindByIdDTO brapiResponseFindByIdDTO = new BrapiResponseFindByIdDTO(new ArrayList<>());
        brapiResponseFindByIdDTO.getResults().add(createStockBrapiFindByIdDTO());
        return brapiResponseFindByIdDTO;
    }

    public static PortfolioDTO createPortfolioDTO(){
        PortfolioDTO portfolioDTO = new PortfolioDTO(createPortfolio());
        return portfolioDTO;
    }

    public static StockPurchaseDTO createStockPurchaseDTO(){
        return new StockPurchaseDTO("AMZO34", "Amazon.inc", 40.0, 10);
    }

    public static PortfolioStock createPortfolioStock(){
        return new PortfolioStock(createPortfolio(), createStock(), 10, 50.0, 500.0);
    }

}
