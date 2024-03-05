package com.devsuperior.investimentos.projection;

public interface UserDetailsProjection {
    String getUsername();
    String getPassword();
    String getAuthority();
    Long getRoleId();
}
