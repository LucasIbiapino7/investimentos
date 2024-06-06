package com.devsuperior.investimentos.testes;

import com.devsuperior.investimentos.client.dto.StockBrapiFindByIdDTO;
import com.devsuperior.investimentos.dto.account.AccountDTO;
import com.devsuperior.investimentos.dto.account.DepositDTO;
import com.devsuperior.investimentos.dto.account.WithdrawDTO;
import com.devsuperior.investimentos.entities.Account;
import com.devsuperior.investimentos.entities.Stock;
import com.devsuperior.investimentos.entities.User;

import java.time.LocalDate;

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
        user.setId(1L);
        user.setFirstName("Lucas");
        user.setLastName("Duarte");
        user.setEmail("lucas@gmail.com");
        user.setBirthDate(LocalDate.now());
        user.setPassword("123456");
        return user;
    }

    public static User createUserWithAccount(){
        User user = new User();
        user.setId(1L);
        user.setFirstName("Lucas");
        user.setLastName("Duarte");
        user.setEmail("lucas@gmail.com");
        user.setPassword("123456");
        user.setBirthDate(LocalDate.now());
        user.setAccount(createAccount());
        return user;
    }

    public static Account createAccount(){
        Account account = new Account();
        account.setId(1L);
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
}
