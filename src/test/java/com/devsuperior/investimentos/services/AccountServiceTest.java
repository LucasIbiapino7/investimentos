package com.devsuperior.investimentos.services;

import com.devsuperior.investimentos.dto.account.AccountDTO;
import com.devsuperior.investimentos.dto.account.BalanceDTO;
import com.devsuperior.investimentos.dto.account.DepositDTO;
import com.devsuperior.investimentos.dto.account.WithdrawDTO;
import com.devsuperior.investimentos.entities.Account;
import com.devsuperior.investimentos.entities.User;
import com.devsuperior.investimentos.repositories.AccountRepository;
import com.devsuperior.investimentos.services.exceptions.AccountException;
import com.devsuperior.investimentos.services.exceptions.PasswordException;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountService service;

    @Mock
    private AccountRepository repository;

    @Mock
    private UserService userService;

    private User userLoggedWithAccount;
    private User userLoggedNoAccount;
    private Account account;
    private AccountDTO accountDTOWithNameNull;
    private AccountDTO accountDTO;
    private DepositDTO depositDTO;
    private WithdrawDTO withdrawDTO;

    @BeforeEach
    void setUp() {

        userLoggedWithAccount = Factory.createUserWithAccount();
        userLoggedNoAccount = Factory.createUser();
        account = Factory.createAccount();
        accountDTO = Factory.createAccountDTO();
        accountDTOWithNameNull = Factory.createAccountDTOWithNameNull();
        depositDTO = Factory.createDepositDTO();
        withdrawDTO = Factory.createWithdrawDTO();

        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(account);

    }

    @Test
    public void insertShouldPersistAndReturnAccountDTOWhenUserLoggedAndNotAccount(){

        Mockito.when(userService.authenticated()).thenReturn(userLoggedNoAccount);

        AccountDTO response = service.insert(accountDTO);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getId(), accountDTO.getId());
        Assertions.assertEquals(response.getName(), accountDTO.getName());

    }

    @Test
    public void insertShouldThrowAccountExceptionWhenUserIsLoggedWithAccount(){

        Mockito.when(userService.authenticated()).thenReturn(userLoggedWithAccount);

        Assertions.assertThrows(AccountException.class, () -> {
            AccountDTO response = service.insert(accountDTO);
        });

    }

    @Test
    public void insertShouldThrowUsernameNotFoundExceptionWhenUserNotLogged(){

        Mockito.when(userService.authenticated()).thenThrow(UsernameNotFoundException.class);

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            AccountDTO response = service.insert(accountDTO);
        });

    }

    @Test
    public void depositShouldThrowResourceNotFoundExceptionWhenUserLoggedAndNoAccount(){

        Mockito.when(userService.authUser(depositDTO.getPassword())).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            BalanceDTO response = service.deposit(depositDTO);
        });

    }

    @Test
    public void depositShouldThrowPasswordExceptionWhenUserLoggedAndPasswordIsIncorrect(){

        Mockito.when(userService.authUser(depositDTO.getPassword())).thenThrow(PasswordException.class);

        Assertions.assertThrows(PasswordException.class, () -> {
            BalanceDTO response = service.deposit(depositDTO);
        });

    }

    @Test
    public void depositShouldThrowUsernameNotFoundExceptionWhenUserNotLogged(){

        Mockito.when(userService.authUser(depositDTO.getPassword())).thenThrow(UsernameNotFoundException.class);

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            BalanceDTO response = service.deposit(depositDTO);
        });

    }

    @Test
    public void depositShouldUpdateAccountBalanceAndPersistWhenUserIsLoggedAnPasswordIsCorrect(){

        Mockito.when(userService.authUser(depositDTO.getPassword())).thenReturn(account);

        BalanceDTO response = service.deposit(depositDTO);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getBalance(), account.getBalance());

    }

    @Test
    public void withdrawShouldThrowResourceNotFoundExceptionWhenUserLoggedAndNoAccount(){

        Mockito.when(userService.authUser(withdrawDTO.getPassword())).thenThrow(ResourceNotFoundException.class);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            BalanceDTO response = service.withdraw(withdrawDTO);
        });

    }

    @Test
    public void withdrawShouldThrowPasswordExceptionWhenUserLoggedAndPasswordIsIncorrect(){

        Mockito.when(userService.authUser(withdrawDTO.getPassword())).thenThrow(PasswordException.class);

        Assertions.assertThrows(PasswordException.class, () -> {
            BalanceDTO response = service.withdraw(withdrawDTO);
        });

    }

    @Test
    public void withdrawShouldThrowUsernameNotFoundExceptionWhenUserNotLogged(){

        Mockito.when(userService.authUser(withdrawDTO.getPassword())).thenThrow(UsernameNotFoundException.class);

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            BalanceDTO response = service.withdraw(withdrawDTO);
        });

    }

    @Test
    public void withdrawShouldUpdateAccountBalanceAndPersistWhenUserIsLoggedAndPasswordIsCorrect(){

        Mockito.when(userService.authUser(withdrawDTO.getPassword())).thenReturn(account);

        BalanceDTO response = service.withdraw(withdrawDTO);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getBalance(), account.getBalance());

    }

    @Test
    public void withdrawShouldThrowAccountExceptionWhenUserIsLoggedAndPasswordIsCorrectButAmountIsBiggerThanBalance(){

        Mockito.when(userService.authUser(withdrawDTO.getPassword())).thenReturn(account);

        account.setBalance(0.0); // setando um valor menor que o do amount

        Assertions.assertThrows(AccountException.class, () -> {
            BalanceDTO response = service.withdraw(withdrawDTO);
        });

    }

    @Test
    public void balanceShouldThrowAccountExceptionWhenUserIsLoggedInNoAccount(){

        Mockito.when(userService.authenticated()).thenReturn(userLoggedNoAccount);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            BalanceDTO response = service.balance();
        });

    }

    @Test
    public void balanceShouldThrowUsernameNotFoundExceptionWhenUserNotLogged(){

        Mockito.when(userService.authenticated()).thenThrow(UsernameNotFoundException.class);

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            BalanceDTO response = service.balance();
        });

    }

    @Test
    public void balanceShouldTakeAccountBalanceWithUserIsLoggedWithAccount(){

        Mockito.when(userService.authenticated()).thenReturn(userLoggedWithAccount);

        BalanceDTO response = service.balance();

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getBalance(), userLoggedWithAccount.getAccount().getBalance());

    }


}