package com.devsuperior.investimentos.services;

import com.devsuperior.investimentos.dto.account.AccountDTO;
import com.devsuperior.investimentos.dto.account.BalanceDTO;
import com.devsuperior.investimentos.dto.account.DepositDTO;
import com.devsuperior.investimentos.dto.account.WithdrawDTO;
import com.devsuperior.investimentos.entities.Account;
import com.devsuperior.investimentos.entities.User;
import com.devsuperior.investimentos.repositories.AccountRepository;
import com.devsuperior.investimentos.services.exceptions.AccountException;
import com.devsuperior.investimentos.services.exceptions.DateException;
import com.devsuperior.investimentos.services.exceptions.PasswordException;
import com.devsuperior.investimentos.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private UserService userService;

    @Transactional
    public AccountDTO insert(AccountDTO dto) {
        User user = userService.authenticated();
        if (user.getAccount() != null){
            throw new AccountException("Usuário já possui uma conta vinculada");
        }
        Account account = new Account();
        account.setName(dto.getName() == null ? ("Account do " + user.getFirstName() + "!") : (dto.getName()));
        account.setDescription(dto.getDescription());
        account.setBalance(0.0);
        account.setPortfolioNumber(0);
        account.setUser(user);
        account = repository.save(account);
        return new AccountDTO(account);
    }

    @Transactional
    public BalanceDTO deposit(DepositDTO dto) {
        Account account = userService.authUser(dto.getPassword());
        account.deposit(dto.getAmount());
        account = repository.save(account);
        return new BalanceDTO(account.getBalance());
    }

    @Transactional
    public BalanceDTO withdraw(WithdrawDTO dto) {
        Account account = userService.authUser(dto.getPassword());
        account.withdraw(dto.getAmount());
        account = repository.save(account);
        return new BalanceDTO(account.getBalance());
    }

    @Transactional(readOnly = true)
    public BalanceDTO balance() {
        User user = userService.authenticated();
        if (user.getAccount() == null){
            throw new ResourceNotFoundException("Nenhuma Conta vinculada a esse usuário!");
        }
        return new BalanceDTO(user.getAccount().getBalance());
    }
}
