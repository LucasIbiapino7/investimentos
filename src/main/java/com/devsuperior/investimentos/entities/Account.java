package com.devsuperior.investimentos.entities;

import com.devsuperior.investimentos.services.exceptions.AccountException;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "tb_account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;
    private Double balance;
    private Integer portfolioNumber;

    @OneToOne
    @MapsId
    private User user;

    public Account() {
    }

    public Account(Long id, String name, String description, Double balance, Integer portfolioNumber, User user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.balance = balance;
        this.portfolioNumber = portfolioNumber;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Integer getPortfolioNumber() {
        return portfolioNumber;
    }

    public void setPortfolioNumber(Integer portfolioNumber) {
        this.portfolioNumber = portfolioNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void deposit(Double amount){
        balance += amount;
    }

    public void withdraw(Double amount){
        if (amount > balance){
            throw new AccountException("Valor de Saque superior ao Salddo");
        }
        balance -= amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
