package com.bank.exception;

public class InsufficientBalanceException extends BadRequestException {
    public InsufficientBalanceException(long accountId) {
        super(String.format("insufficient balance of account %d", accountId));
    }
}
