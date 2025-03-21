package com.bank.domain.service.impl;

import com.bank.domain.model.Transaction;
import com.bank.domain.model.TransactionStatus;
import com.bank.domain.repository.AccountRepository;
import com.bank.domain.repository.TransactionRepository;
import com.bank.domain.service.TransactionExecutor;
import com.bank.exception.*;
import com.bank.exception.base.InternalServerErrorException;
import com.bank.exception.base.InvalidArgumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import java.time.Instant;

@Component
public class TransactionExecutorImpl implements TransactionExecutor {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionExecutorImpl(@Qualifier("cachedAccountRepository") AccountRepository accountRepository,
                                   @Qualifier("cachedTransactionRepository") TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional(timeout = 3)
    // TODO: limit lock wait timeout instead of db transaction timeout
    public void execute(Transaction transaction) throws InternalServerErrorException, InvalidArgumentException {
        if (transaction.getStatus() != TransactionStatus.PENDING) {
            throw new InvalidTransactionStatusException(transaction.getId(), transaction.getStatus(), TransactionStatus.PENDING);
        }

        var optionalBalance = accountRepository.getBalanceForUpdate(transaction.getFromAccountId());
        if (optionalBalance.isEmpty()) {
            throw new InternalServerErrorException(String.format("Unable to get balance of account %d", transaction.getFromAccountId()));
        }

        var balance = optionalBalance.get();
        if (balance.compareTo(transaction.getAmount()) < 0) {
            throw new InsufficientBalanceException(transaction.getFromAccountId());
        }

        var rowsCount = accountRepository.changeBalance(transaction.getFromAccountId(), transaction.getAmount().negate());
        if (rowsCount != 1) {
            throw new InternalServerErrorException(String.format("Unable to change balance of account %d", transaction.getFromAccountId()));
        }

        rowsCount = accountRepository.changeBalance(transaction.getToAccountId(), transaction.getAmount());
        if (rowsCount != 1) {
            throw new InternalServerErrorException(String.format("Unable to change balance of account %d", transaction.getFromAccountId()));
        }

        if (!transactionRepository.changeStatusAndCompletedAt(transaction.getId(),
                TransactionStatus.PENDING,
                TransactionStatus.SUCCEEDED,
                Instant.now())) {
            throw new InternalServerErrorException(String.format("Unable to change status of transaction %d", transaction.getId()));
        }
    }
}
