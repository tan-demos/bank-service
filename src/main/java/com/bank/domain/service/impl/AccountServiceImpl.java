package com.bank.domain.service.impl;

import com.bank.domain.model.Account;
import com.bank.domain.model.Page;
import com.bank.domain.repository.AccountRepository;
import com.bank.domain.service.AccountService;
import com.bank.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    private final static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(@Qualifier("cachedAccountRepository") AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account add(long id, BigDecimal balance) {
        if (id <= 0) {
            throw new BadRequestException("Invalid param: accountId");
        }

        if (balance == null) {
            balance = BigDecimal.ZERO;
        }

        if (balance.signum() < 0) {
            throw new BadRequestException("Invalid param: balance " + balance.toPlainString());
        }
        var account = Account.builder().id(id).balance(balance).build();
        accountRepository.insert(account);
        logger.info("Added account {}", account.getId());
        return account;
    }

    @Override
    public Optional<Account> getById(long id) {
        return accountRepository.getById(id);
    }

    @Override
    public Page<Account> getPage(int page, int size) {
        return accountRepository.getPage(page, size);
    }
}
