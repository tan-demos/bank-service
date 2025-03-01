package com.bank.exception;

public class AccountNotFoundException extends NotFoundException {
    public AccountNotFoundException(long accountId) {
        super(String.format("account %d not found", accountId));
    }
}
