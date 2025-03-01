package com.bank.infra.redis;

import com.bank.domain.model.Transaction;
import com.bank.domain.model.TransactionStatus;
import com.bank.domain.repository.TransactionRepository;

import java.sql.Timestamp;
import java.util.Optional;


public class CachedTransactionRepository implements TransactionRepository {
    @Override
    public void insert(Transaction transaction) {

    }

    @Override
    public Optional<Transaction> getById(long id) {
        return Optional.empty();
    }

    @Override
    public boolean changeStatusAndCompletedAt(long id, TransactionStatus oldStatus, TransactionStatus newStatus, Timestamp completedAt) {
        return false;
    }
}
