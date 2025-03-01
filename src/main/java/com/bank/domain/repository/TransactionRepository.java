package com.bank.domain.repository;

import com.bank.domain.model.Transaction;
import com.bank.domain.model.TransactionStatus;

import java.sql.Timestamp;
import java.util.Optional;


public interface TransactionRepository {
    void insert(Transaction transaction);
    Optional<Transaction> getById(long id);
    boolean changeStatusAndCompletedAt(long id, TransactionStatus oldStatus, TransactionStatus newStatus, Timestamp completedAt);
}
