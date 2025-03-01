package com.bank.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@Builder
@Data
public class Transaction {
    private long id;
    private TransactionType type;
    private long fromAccountId;
    private long toAccountId;
    private BigDecimal amount;
    private TransactionStatus status;
    private Instant createdAt;
    private Instant completedAt;
}
