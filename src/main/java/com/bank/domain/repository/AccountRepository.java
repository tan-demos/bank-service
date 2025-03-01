package com.bank.domain.repository;

import com.bank.domain.model.Account;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountRepository {
    void insert(Account account);
    Optional<Account> getById(long id);
    Optional<BigDecimal> getBalanceForUpdate(long id);
    int changeBalance(long id, BigDecimal change);
}
