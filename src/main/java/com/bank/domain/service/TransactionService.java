package com.bank.domain.service;

import com.bank.domain.model.Transaction;
import com.bank.domain.model.CreateTransactionParams;
import com.bank.domain.repository.PagingRepository;

import java.util.Optional;


public interface TransactionService extends PagingRepository<Transaction> {
    Transaction create(CreateTransactionParams params);

    Transaction submit(long id);

    Optional<Transaction> getById(long id);
}
