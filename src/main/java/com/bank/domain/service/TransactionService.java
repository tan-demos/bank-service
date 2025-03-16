package com.bank.domain.service;

import com.bank.domain.model.Transaction;
import com.bank.domain.model.CreateTransactionParams;
import com.bank.domain.repository.PagingRepository;
import com.bank.exception.base.InternalServerErrorException;
import com.bank.exception.base.InvalidArgumentException;

import java.util.Optional;


public interface TransactionService extends PagingRepository<Transaction> {
    Transaction create(CreateTransactionParams params) throws InvalidArgumentException;

    Transaction submit(long id) throws InvalidArgumentException, InternalServerErrorException;

    Optional<Transaction> getById(long id);
}
