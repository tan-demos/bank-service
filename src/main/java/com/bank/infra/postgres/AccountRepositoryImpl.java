package com.bank.infra.postgres;

import com.bank.domain.model.Account;
import com.bank.domain.model.Page;
import com.bank.infra.postgres.mapper.AccountMapper;
import com.bank.domain.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository("defaultAccountRepository")
public class AccountRepositoryImpl implements AccountRepository {
    private final AccountMapper mapper;

    @Autowired
    public AccountRepositoryImpl(AccountMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void insert(Account account) {
        mapper.insert(account);
    }

    @Override
    public Optional<Account> getById(long id) {
        return mapper.getById(id);
    }

    @Override
    public Optional<BigDecimal> getBalanceForUpdate(long id) {
        return mapper.getBalanceForUpdate(id);
    }
    @Override
    public int changeBalance(long id, BigDecimal change) {
        return mapper.changeBalance(id, change);
    }

    @Override
    public Page<Account> getPage(int page, int size) {
        var data = mapper.list(page * size, size);
        var total = mapper.count();
        return Page.<Account>builder().data(data).page(page).size(size).totalCount(total).build();
    }
}
