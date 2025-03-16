package com.bank.domain.service.impl;

import com.bank.TestUtil;
import com.bank.domain.model.Transaction;
import com.bank.domain.model.TransactionStatus;
import com.bank.domain.repository.AccountRepository;
import com.bank.domain.repository.TransactionRepository;
import com.bank.exception.*;
import com.bank.exception.base.InternalServerErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionExecutorImplTests {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionExecutorImpl transactionExecutor;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        transaction = TestUtil.randomDomainTransaction();
        transaction.setStatus(TransactionStatus.PENDING);
    }

    @Test
    void testExecuteTransaction() {
        when(accountRepository.getBalanceForUpdate(transaction.getFromAccountId())).thenReturn(Optional.of(transaction.getAmount().add(new BigDecimal("1"))));
        when(accountRepository.changeBalance(transaction.getFromAccountId(), transaction.getAmount().negate())).thenReturn(1);
        when(accountRepository.changeBalance(transaction.getToAccountId(),transaction.getAmount())).thenReturn(1);
        when(transactionRepository.changeStatusAndCompletedAt(eq(transaction.getId()), eq(TransactionStatus.PENDING), eq(TransactionStatus.SUCCEEDED), any(Instant.class))).thenReturn(true);

        assertDoesNotThrow(() -> transactionExecutor.execute(transaction));

        verify(accountRepository, times(1)).getBalanceForUpdate(transaction.getFromAccountId());
        verify(accountRepository, times(1)).changeBalance(transaction.getFromAccountId(), transaction.getAmount().negate());
        verify(accountRepository, times(1)).changeBalance(transaction.getToAccountId(),transaction.getAmount());
        verify(transactionRepository, times(1)).changeStatusAndCompletedAt(eq(transaction.getId()), eq(TransactionStatus.PENDING), eq(TransactionStatus.SUCCEEDED), any(Instant.class));
    }

    @Test
    void testExecuteTransactionInvalidStatus() {
        transaction.setStatus(TransactionStatus.SUCCEEDED);

        assertThrows(InvalidTransactionStatusException.class, () -> transactionExecutor.execute(transaction));

        verify(accountRepository, never()).getBalanceForUpdate(anyLong());
        verify(accountRepository, never()).changeBalance(anyLong(), any());
        verify(transactionRepository, never()).changeStatusAndCompletedAt(anyLong(), any(), any(), any());
    }

    @Test
    void testExecuteTransactionAccountNotFound() {
        when(accountRepository.getBalanceForUpdate(transaction.getFromAccountId())).thenReturn(Optional.empty());

        assertThrows(InternalServerErrorException.class, () -> transactionExecutor.execute(transaction));

        verify(accountRepository, times(1)).getBalanceForUpdate(transaction.getFromAccountId());
        verify(accountRepository, never()).changeBalance(anyLong(), any());
        verify(transactionRepository, never()).changeStatusAndCompletedAt(anyLong(), any(), any(), any());
    }

    @Test
    void testExecuteTransactionInsufficientBalance() {
        when(accountRepository.getBalanceForUpdate(transaction.getFromAccountId())).thenReturn(Optional.of(transaction.getAmount().subtract(new BigDecimal(1))));

        assertThrows(InsufficientBalanceException.class, () -> transactionExecutor.execute(transaction));

        verify(accountRepository, times(1)).getBalanceForUpdate(transaction.getFromAccountId());
        verify(accountRepository, never()).changeBalance(anyLong(), any());
        verify(transactionRepository, never()).changeStatusAndCompletedAt(anyLong(), any(), any(), any());
    }

    @Test
    void testExecuteTransactionChangeBalanceFailed() {
        // TODO:
    }

    @Test
    void testExecuteTransactionChangeStatusFailed() {
        // TODO:
    }
}