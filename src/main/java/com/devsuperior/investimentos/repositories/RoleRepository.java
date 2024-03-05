package com.devsuperior.investimentos.repositories;

import com.devsuperior.investimentos.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByAuthority(String authority);
}
