package com.bank.infra.postgres;

import com.bank.domain.model.Transaction;
import com.bank.domain.model.TransactionStatus;
import com.bank.infra.postgres.mapper.TransactionMapper;
import com.bank.domain.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Optional;

public class TransactionRepositoryImpl implements TransactionRepository {
    @Autowired
    private TransactionMapper mapper;

    @Override
    public void insert(Transaction transaction) {
        var id = mapper.insert(transaction);
        transaction.setId(id);
    }

    @Override
    public Optional<Transaction> getById(long id) {
        return mapper.getById(id);
    }

    @Override
    public boolean changeStatusAndCompletedAt(long id, TransactionStatus oldStatus, TransactionStatus newStatus, Instant completedAt) {
        var count = mapper.changeStatusAndCompletedAt(id, oldStatus, newStatus, completedAt);
        return count > 0;
    }
}
