package com.bank.exception;

import com.bank.exception.base.NotFoundException;

public class AccountNotFoundException extends NotFoundException {
    public AccountNotFoundException(long accountId) {
        super(String.format("account %d not found", accountId));
    }
}
