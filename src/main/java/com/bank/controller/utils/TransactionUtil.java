package com.bank.controller.utils;

import com.bank.api.model.TransactionStatus;
import com.bank.api.model.TransactionType;
import com.bank.api.model.Transaction;
public class TransactionUtil {
    public static Transaction fromDomain(com.bank.domain.model.Transaction transaction) {
        return new Transaction()
                .id(transaction.getId())
                .type(transaction.getType().getValue())
                .fromAccountId(transaction.getFromAccountId())
                .toAccountId(transaction.getToAccountId())
                .amount(transaction.getAmount().toPlainString())
                .status(transaction.getStatus().getValue())
                .createdAt(transaction.getCreatedAt() != null ? transaction.getCreatedAt().getEpochSecond() : 0)
                .completedAt(transaction.getCompletedAt() != null ? transaction.getCompletedAt().getEpochSecond() : 0);
    }

    public static void populateApiTransaction(com.bank.domain.model.Transaction source, Transaction dest) {
        dest.setId(source.getId());
        dest.setType(source.getType().getValue());
        dest.setFromAccountId(source.getFromAccountId());
        dest.setToAccountId(source.getToAccountId());
        dest.setAmount(source.getAmount().toPlainString());
        dest.setStatus(source.getStatus().getValue());
        dest.setCreatedAt(source.getCreatedAt() != null ? source.getCreatedAt().getEpochSecond() : 0);
        dest.setCompletedAt(source.getCompletedAt() != null ? source.getCompletedAt().getEpochSecond() : 0);
    }

    public static TransactionType fromDomainType(com.bank.domain.model.TransactionType type) {
        return switch (type) {
            case DEPOSIT -> TransactionType.DEPOSIT;
            case WITHDRAWAL -> TransactionType.WITHDRAWAL;
            case TRANSFER -> TransactionType.TRANSFER;
        };
    }

    public static com.bank.domain.model.TransactionType toDomainType(TransactionType type) {
        return switch (type) {
            case DEPOSIT -> com.bank.domain.model.TransactionType.DEPOSIT;
            case WITHDRAWAL -> com.bank.domain.model.TransactionType.WITHDRAWAL;
            case TRANSFER -> com.bank.domain.model.TransactionType.TRANSFER;
        };
    }

    public static TransactionStatus fromDomainStatus(com.bank.domain.model.TransactionStatus status) {
        return switch (status) {
            case PENDING -> TransactionStatus.PENDING;
            case SUCCEEDED -> TransactionStatus.SUCCEEDED;
            case FAILED -> TransactionStatus.FAILED;
        };
    }
}
