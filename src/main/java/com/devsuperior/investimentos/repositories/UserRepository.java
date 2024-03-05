package com.devsuperior.investimentos.repositories;

import com.devsuperior.investimentos.entities.User;
import com.devsuperior.investimentos.projection.UserDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(nativeQuery = true, value = """
            SELECT  email AS username, password, tb_role.id AS roleId, tb_role.authority
            FROM tb_user
            INNER JOIN tb_user_role ON tb_user_role.user_id = tb_user.id
            INNER JOIN tb_role ON tb_user_role.role_id = tb_role.id
            WHERE email = :email
            """)
    List<UserDetailsProjection> searchUserAndRolesByEmail(String email);
}
