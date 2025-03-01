package com.bank.exception;



public class TransactionNotFoundException extends NotFoundException {
    public TransactionNotFoundException(long transactionId) {
        super(String.format("transaction %s not found", transactionId));
    }
}
