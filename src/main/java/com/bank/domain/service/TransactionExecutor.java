package com.bank.domain.service;

import com.bank.domain.model.Transaction;

public interface TransactionExecutor {
    void execute(Transaction transaction);
}
