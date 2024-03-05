package com.devsuperior.investimentos.repositories;

import com.devsuperior.investimentos.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
