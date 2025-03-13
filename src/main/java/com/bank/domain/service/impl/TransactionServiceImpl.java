package com.bank.domain.service.impl;

import com.bank.domain.model.*;
import com.bank.domain.pubsub.Producer;
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
    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);
    private static final String TOPIC_TRANSACTION_CREATED = "transaction-created";

    private final AccountRepository accountRepository;

    private final TransactionRepository transactionRepository;

    private final TransactionExecutor transactionExecutor;

    private final Producer producer;


    @Autowired
    public TransactionServiceImpl(@Qualifier("cachedAccountRepository") AccountRepository accountRepository,
                                  @Qualifier("cachedTransactionRepository") TransactionRepository transactionRepository,
                                  TransactionExecutor transactionExecutor,
                                  @Qualifier("kafkaProducer") Producer producer) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.transactionExecutor = transactionExecutor;
        this.producer = producer;
    }

    @Override
    public Transaction create(CreateTransactionParams params) {
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
                .orElseThrow(() -> new AccountNotFoundException(params.getFromAccountId()));

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
        producer.send(TOPIC_TRANSACTION_CREATED, transaction);
        logger.info("Published {} event {}", TOPIC_TRANSACTION_CREATED, transaction.getId());
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

        logger.info("Starting transaction {}: {}->{} {}",
                transaction.getId(),
                transaction.getFromAccountId(),
                transaction.getToAccountId(),
                transaction.getAmount());

        transactionExecutor.execute(transaction);
        logger.info("Completed transaction: id={}, status={}, completedAt={}", transaction.getId(), transaction.getStatus(), transaction.getCompletedAt());
        return transactionRepository.getById(transactionId).orElseThrow();
    }

    @Override
    public Optional<Transaction> getById(long transactionId) {
        return transactionRepository.getById(transactionId);
    }

    @Override
    public Page<Transaction> getPage(int page, int size) {
        return transactionRepository.getPage(page, size);
    }
}
