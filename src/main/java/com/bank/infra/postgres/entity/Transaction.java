package com.bank.infra.postgres.entity;

import com.bank.domain.model.TransactionStatus;
import com.bank.domain.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Optional;

@AllArgsConstructor
@Builder
@Data
public class Transaction {
    private long id;
    private int type;
    private long fromAccountId;
    private long toAccountId;
    private BigDecimal amount;
    private int status;
    private Timestamp createdAt;
    private Timestamp completedAt;

    public static Transaction fromDomain(com.bank.domain.model.Transaction transaction) {
        var entity = Transaction.builder()
                .id(transaction.getId())
                .type(transaction.getType().getValue())
                .fromAccountId(transaction.getFromAccountId())
                .toAccountId(transaction.getToAccountId())
                .amount(transaction.getAmount())
                .status(transaction.getStatus().getValue())
                .createdAt(new Timestamp(transaction.getCreatedAt().toEpochMilli()))
                .build();
        if (transaction.getCompletedAt() != null) {
            entity.setCompletedAt(new Timestamp(transaction.getCompletedAt().toEpochMilli()));
        }
        return entity;
    }

    public static com.bank.domain.model.Transaction toDomain(Transaction transaction) {
        return com.bank.domain.model.Transaction.builder()
                .id(transaction.getId())
                .type(TransactionType.fromValue(transaction.getType()))
                .fromAccountId(transaction.getFromAccountId())
                .toAccountId(transaction.getToAccountId())
                .amount(transaction.getAmount())
                .status(TransactionStatus.fromValue(transaction.getStatus()))
                .createdAt(transaction.getCreatedAt().toInstant())
                .completedAt(transaction.getCompletedAt() != null ? transaction.getCompletedAt().toInstant() : null)
                .build();
    }
}