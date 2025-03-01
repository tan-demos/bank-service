package com.bank.infra.redis;

import com.bank.domain.model.Account;
import com.bank.domain.repository.AccountRepository;

import java.math.BigDecimal;
import java.util.Optional;

// TODO: cache account data for read requests

public class CachedAccountRepository implements AccountRepository {
    private final AccountRepository delegate;

    public CachedAccountRepository(AccountRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public void insert(Account account) {
        // write into database
        // delete from cache
    }

    @Override
    public Optional<Account> getById(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<BigDecimal> getBalanceForUpdate(long id) {
        // TODO: this method should always call database directly
        return Optional.empty();
    }

    @Override
    public int changeBalance(long id, BigDecimal change) {
        // write into database
        // delete from cache
        return 0;
    }
}
