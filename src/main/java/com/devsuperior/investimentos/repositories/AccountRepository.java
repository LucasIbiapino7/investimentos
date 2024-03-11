package com.devsuperior.investimentos.repositories;

import com.devsuperior.investimentos.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
