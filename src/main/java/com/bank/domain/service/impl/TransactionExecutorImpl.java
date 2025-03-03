package com.bank.domain.service.impl;

import com.bank.domain.model.Transaction;
import com.bank.domain.model.TransactionStatus;
import com.bank.domain.repository.AccountRepository;
import com.bank.domain.repository.TransactionRepository;
import com.bank.domain.service.TransactionExecutor;
import com.bank.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;

public class TransactionExecutorImpl implements TransactionExecutor {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    @Transactional(timeout = 3)
    // TODO: limit lock wait timeout instead of db transaction timeout
    public void execute(Transaction transaction) {
        if (transaction.getStatus() != TransactionStatus.PENDING) {
            throw new InvalidTransactionStatusException(transaction.getId(), transaction.getStatus(), TransactionStatus.PENDING);
        }

        var optionalBalance = accountRepository.getBalanceForUpdate(transaction.getFromAccountId());
        if (optionalBalance.isEmpty()) {
            throw new AccountNotFoundException(transaction.getFromAccountId());
        }

        var balance = optionalBalance.get();
        if (balance.compareTo(transaction.getAmount()) < 0) {
            throw new InsufficientBalanceException(transaction.getFromAccountId());
        }

        var rowsCount = accountRepository.changeBalance(transaction.getFromAccountId(), transaction.getAmount().negate());
        if (rowsCount != 1) {
            throw new IllegalStateException(String.format("unable to change balance of account %d", transaction.getFromAccountId()));
        }

        rowsCount = accountRepository.changeBalance(transaction.getToAccountId(), transaction.getAmount());
        if (rowsCount != 1) {
            throw new IllegalStateException(String.format("unable to change balance of account %d", transaction.getFromAccountId()));
        }

        if (!transactionRepository.changeStatusAndCompletedAt(transaction.getId(),
                TransactionStatus.PENDING,
                TransactionStatus.SUCCEEDED,
                Timestamp.from(Instant.now()))) {
            throw new IllegalStateException(String.format("unable to change status of transaction %d", transaction.getId()));
        }
    }
}
