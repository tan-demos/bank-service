package com.bank.exception;

import com.bank.exception.base.InvalidArgumentException;

public class InsufficientBalanceException extends InvalidArgumentException {
    public InsufficientBalanceException(long accountId) {
        super(String.format("insufficient balance of account %d", accountId));
    }
}
