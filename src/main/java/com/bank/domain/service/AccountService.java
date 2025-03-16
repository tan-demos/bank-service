package com.bank.domain.service;

import com.bank.domain.model.Account;
import com.bank.domain.repository.PagingRepository;
import com.bank.exception.base.InvalidArgumentException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface AccountService extends PagingRepository<Account> {
    Account add(long id, BigDecimal balance) throws InvalidArgumentException;
    Optional<Account> getById(long id);
    List<Account> batchGet(List<Long> ids);
}
