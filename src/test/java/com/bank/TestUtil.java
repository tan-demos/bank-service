package com.bank;

import com.bank.domain.model.Account;
import com.bank.domain.model.Transaction;
import com.bank.domain.model.TransactionStatus;
import com.bank.domain.model.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Random;

public class TestUtil {
    public static long randomPositiveLong() {
        return new Random().nextLong(1, Long.MAX_VALUE);
    }

    public static BigDecimal randomPositiveDecimal() {
        return BigDecimal.valueOf(new Random().nextDouble(0.1, Double.MAX_VALUE));
    }

    public static BigDecimal randomNegativeDecimal() {
        return BigDecimal.valueOf(new Random().nextDouble(-Double.MAX_VALUE, 0));
    }

    public static TransactionType randomDomainTransactionType() {
        return TransactionType.fromValue(new Random().nextInt(0, 3));
    }

    public static TransactionStatus randomDomainTransactionStatus() {
        return TransactionStatus.fromValue(new Random().nextInt(0, 3));
    }

    public static Transaction randomDomainTransaction() {
        return Transaction.builder()
                .id(randomPositiveLong())
                .type(randomDomainTransactionType())
                .fromAccountId(randomPositiveLong())
                .toAccountId(randomPositiveLong())
                .amount(randomPositiveDecimal())
                .status(randomDomainTransactionStatus())
                .createdAt(Instant.now())
                .completedAt(Instant.now())
                .build();
    }

    public static Account randomDomainAccount() {
        return Account.builder()
                .id(randomPositiveLong())
                .balance(randomPositiveDecimal())
                .build();
    }
}
