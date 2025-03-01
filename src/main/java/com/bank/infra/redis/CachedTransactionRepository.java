package com.bank.infra.redis;

import com.bank.domain.model.Transaction;
import com.bank.domain.model.TransactionStatus;
import com.bank.domain.repository.TransactionRepository;

import java.sql.Timestamp;
import java.util.Optional;


// TODO: cache transaction data for read requests

public class CachedTransactionRepository implements TransactionRepository {
    private final TransactionRepository delegate;

    public CachedTransactionRepository(TransactionRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public void insert(Transaction transaction) {
        // write into database
        // delete from cache
    }

    @Override
    public Optional<Transaction> getById(long id) {
        // TODO:
        // read from cache
        // read from db if not found
        // update cache
        return Optional.empty();
    }

    @Override
    public boolean changeStatusAndCompletedAt(long id, TransactionStatus oldStatus, TransactionStatus newStatus, Timestamp completedAt) {
        // write into database
        // delete from cache
        return false;
    }
}
