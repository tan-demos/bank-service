package com.bank.domain.service;

import com.bank.domain.model.Account;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountService {
    Account add(long id, BigDecimal balance);
    Optional<Account> getById(long id);
}
