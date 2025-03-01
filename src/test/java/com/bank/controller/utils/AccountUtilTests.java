package com.bank.controller.utils;

import com.bank.TestUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountUtilTests {
    @Test
    void fromDomain_mapsAllFieldsCorrectly() {
        var domainAccount = TestUtil.randomDomainAccount();
        var apiAccount = AccountUtil.fromDomain(domainAccount);
        assertEquals(domainAccount.getId(), apiAccount.getId());
        assertEquals(domainAccount.getBalance().toPlainString(), apiAccount.getBalance());
    }
}
