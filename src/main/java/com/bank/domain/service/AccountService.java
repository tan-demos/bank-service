package com.bank.domain.service;

import com.bank.domain.model.Account;
import com.bank.domain.repository.PagingRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface AccountService extends PagingRepository<Account> {
    Account add(long id, BigDecimal balance);
    Optional<Account> getById(long id);
}
