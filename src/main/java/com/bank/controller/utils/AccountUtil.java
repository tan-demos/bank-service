package com.bank.controller.utils;


import com.bank.api.model.Account;

public class AccountUtil {
    public static Account fromDomain(com.bank.domain.model.Account account) {
        return new Account().
                id(account.getId()).
                balance(account.getBalance().toPlainString());
    }
}
