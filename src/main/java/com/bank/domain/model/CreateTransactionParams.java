package com.bank.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateTransactionParams {
    private TransactionType type;
    private long fromAccountId;
    private long toAccountId;
    private BigDecimal amount;
}
