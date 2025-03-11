package com.bank.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction implements Serializable {
    private long id;
    private TransactionType type;
    private long fromAccountId;
    private long toAccountId;
    private BigDecimal amount;
    private TransactionStatus status;
    private Instant createdAt;
    private Instant completedAt;
}
