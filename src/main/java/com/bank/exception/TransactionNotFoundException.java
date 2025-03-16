package com.bank.exception;


import com.bank.exception.base.NotFoundException;

public class TransactionNotFoundException extends NotFoundException {
    public TransactionNotFoundException(long transactionId) {
        super(String.format("transaction %s not found", transactionId));
    }
}
