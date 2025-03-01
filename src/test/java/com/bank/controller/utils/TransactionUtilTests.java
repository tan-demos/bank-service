package com.bank.controller.utils;

import com.bank.TestUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionUtilTests {
    @Test
    void fromDomain_mapsAllFieldsCorrectly() {
        var domainTransaction = TestUtil.randomDomainTransaction();
        var apiTransaction = TransactionUtil.fromDomain(domainTransaction);
        assertEquals(domainTransaction.getId(), apiTransaction.getId());
        assertEquals(domainTransaction.getType().getValue(), apiTransaction.getType());
        assertEquals(domainTransaction.getFromAccountId(), apiTransaction.getFromAccountId());
        assertEquals(domainTransaction.getToAccountId(), apiTransaction.getToAccountId());
        assertEquals(domainTransaction.getAmount().toPlainString(), apiTransaction.getAmount());
        assertEquals(domainTransaction.getStatus().getValue(), apiTransaction.getStatus());
    }
}
