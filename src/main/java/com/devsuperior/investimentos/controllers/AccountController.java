package com.devsuperior.investimentos.controllers;

import com.devsuperior.investimentos.dto.UserDTO;
import com.devsuperior.investimentos.dto.UserDeleteDTO;
import com.devsuperior.investimentos.dto.UserInsertDTO;
import com.devsuperior.investimentos.dto.account.AccountDTO;
import com.devsuperior.investimentos.dto.account.BalanceDTO;
import com.devsuperior.investimentos.dto.account.DepositDTO;
import com.devsuperior.investimentos.dto.account.WithdrawDTO;
import com.devsuperior.investimentos.services.AccountService;
import com.devsuperior.investimentos.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService service;

    @PreAuthorize("hasAnyRole('ROLE_CLIENT')")
    @PostMapping
    public ResponseEntity<AccountDTO> insert(@RequestBody @Valid AccountDTO dto){//MUDAR PRA 201
        dto = service.insert(dto);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT')")
    @PostMapping("/deposit")
    public ResponseEntity<BalanceDTO> deposit(@RequestBody @Valid DepositDTO dto){
        BalanceDTO balanceDTO = service.deposit(dto);
        return ResponseEntity.ok(balanceDTO);
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT')")
    @PostMapping("/withdraw")
    public ResponseEntity<BalanceDTO> withdraw(@RequestBody @Valid WithdrawDTO dto){
        BalanceDTO balanceDTO =  service.withdraw(dto);
        return ResponseEntity.ok(balanceDTO);
    }

    @PreAuthorize("hasAnyRole('ROLE_CLIENT')")
    @GetMapping("/balance")
    public ResponseEntity<BalanceDTO> balance(){
        BalanceDTO balanceDTO= service.balance();
        return ResponseEntity.ok(balanceDTO);
    }

}
