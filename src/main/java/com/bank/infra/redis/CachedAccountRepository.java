package com.bank.infra.redis;

import com.bank.domain.model.Account;
import com.bank.domain.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.Optional;

public class CachedAccountRepository implements AccountRepository {

    @Override
    public void insert(Account account) {

    }

    @Override
    public Optional<Account> getById(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<BigDecimal> getBalanceForUpdate(long id) {
        return Optional.empty();
    }

    @Override
    public int changeBalance(long id, BigDecimal change) {
        return 0;
    }
}
