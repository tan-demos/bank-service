package com.bank.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Builder
@Data
public class CreateTransactionParams {
    private TransactionType type;
    private long fromAccountId;
    private long toAccountId;
    private BigDecimal amount;
}
