package com.bank.domain.service;

import com.bank.domain.model.Transaction;
import com.bank.exception.InvalidTransactionStatusException;
import com.bank.exception.base.InternalServerErrorException;
import com.bank.exception.base.InvalidArgumentException;

public interface TransactionExecutor {
    void execute(Transaction transaction) throws InvalidArgumentException, InternalServerErrorException;
}
