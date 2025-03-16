package com.bank.exception;

import com.bank.domain.model.TransactionStatus;
import com.bank.exception.base.InvalidArgumentException;


public class InvalidTransactionStatusException extends InvalidArgumentException {
    public InvalidTransactionStatusException(long transactionId, TransactionStatus currentStatus, TransactionStatus expectedStatus) {
        super(String.format("transaction %s: current status %s, expected status %s", transactionId, currentStatus, expectedStatus));
    }
}
