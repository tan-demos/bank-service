package com.bank.domain.service.impl;

import com.bank.domain.model.CreateTransactionParams;
import com.bank.domain.model.Transaction;
import com.bank.domain.model.TransactionStatus;
import com.bank.domain.model.TransactionType;
import com.bank.domain.repository.AccountRepository;
import com.bank.domain.repository.TransactionRepository;
import com.bank.domain.service.TransactionExecutor;
import com.bank.domain.service.TransactionService;
import com.bank.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    @Qualifier("cachedAccountRepository")
    private AccountRepository accountRepository;
    @Autowired
    @Qualifier("cachedTransactionRepository")
    private TransactionRepository transactionRepository;
    @Autowired
    private TransactionExecutor transactionExecutor;

    private final static Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    @Override
    public Transaction create(CreateTransactionParams params)  {
        if (params.getType() != TransactionType.TRANSFER) {
            throw new BadRequestException("Invalid transaction type: only support transfer");
        }

        if (params.getFromAccountId() <= 0) {
            throw new BadRequestException("Invalid param: fromAccountId");
        }

        if (params.getToAccountId() <= 0) {
            throw new BadRequestException("Invalid param: toAccountId");
        }

        if (params.getFromAccountId() == params.getToAccountId()) {
            throw new BadRequestException("Cannot transfer to the same account");
        }

        if (params.getAmount() == null || params.getAmount().signum() <= 0) {
            throw new BadRequestException("Invalid param: amount");
        }

        var fromAccount = accountRepository.getById(params.getFromAccountId())
                .orElseThrow(() -> new AccountNotFoundException(params.getFromAccountId()));
        if (fromAccount.getBalance().compareTo(params.getAmount()) < 0) {
            throw new InsufficientBalanceException(fromAccount.getId());
        }

        accountRepository.getById(params.getToAccountId())
                .orElseThrow(()-> new AccountNotFoundException(params.getFromAccountId()));

        var transaction = Transaction.builder().
                type(params.getType()).
                fromAccountId(params.getFromAccountId()).
                toAccountId(params.getToAccountId()).
                amount(params.getAmount()).
                createdAt(Instant.now()).
                status(TransactionStatus.PENDING).
                build();
        transactionRepository.insert(transaction);
        logger.info("Created transaction {}", transaction.getId());
        return transaction;
    }

    @Override
    public Transaction submit(long transactionId) {
        var optionalTransaction = transactionRepository.getById(transactionId);
        if (optionalTransaction.isEmpty()) {
            throw new TransactionNotFoundException(transactionId);
        }

        var transaction = optionalTransaction.get();
        if (transaction.getStatus() != TransactionStatus.PENDING) {
            return transaction;
        }

        transactionExecutor.execute(transaction);
        logger.info("Completed transaction {}", transaction.getId());
        return transactionRepository.getById(transactionId).orElseThrow();
    }

    @Override
    public Optional<Transaction> getById(long transactionId) {
        return transactionRepository.getById(transactionId);
    }
}
