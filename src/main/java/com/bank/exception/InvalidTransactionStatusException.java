package com.bank.exception;

import com.bank.domain.model.TransactionStatus;



public class InvalidTransactionStatusException extends BadRequestException {
    public InvalidTransactionStatusException(long transactionId, TransactionStatus currentStatus, TransactionStatus expectedStatus) {
        super(String.format("transaction %s: current status %s, expected status %s", transactionId, currentStatus, expectedStatus));
    }
}
