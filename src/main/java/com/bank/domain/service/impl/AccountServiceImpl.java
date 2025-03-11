package com.bank.domain.service.impl;

import com.bank.domain.model.Account;
import com.bank.domain.model.Page;
import com.bank.domain.repository.AccountRepository;
import com.bank.domain.service.AccountService;
import com.bank.exception.BadRequestException;
import com.bank.util.concurrent.NamedThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.*;

@Service
public class AccountServiceImpl implements AccountService {
    private final static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    private final AccountRepository accountRepository;
    private final ExecutorService executorService;
    private static final int THREAD_POOL_SIZE = 10;

    @Autowired
    public AccountServiceImpl(@Qualifier("cachedAccountRepository") AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE, new NamedThreadFactory("AccountService"));
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
    public List<Account> batchGet(List<Long> ids) {
        List<Account> accounts = new ArrayList<>(ids.size());

        ExecutorCompletionService<Optional<Account>> completionService = new ExecutorCompletionService<>(executorService);
        for (long id: ids) {
            final var accountId = id;
            completionService.submit(() -> accountRepository.getById(accountId));
        }

        for (int i = 0; i < ids.size(); i++) {
            try {
                var optionalAccount = completionService.take().get();
                optionalAccount.ifPresent(accounts::add);
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        return accounts;
    }

    @Override
    public Page<Account> getPage(int page, int size) {
        return accountRepository.getPage(page, size);
    }
}
