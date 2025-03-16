package com.bank.controller;

import com.bank.TestUtil;
import com.bank.api.model.Transaction;
import com.bank.api.model.CreateTransactionRequest;
import com.bank.api.model.TransactionType;
import com.bank.controller.api.TransactionController;
import com.bank.domain.service.TransactionService;
import com.bank.domain.model.CreateTransactionParams;
import com.bank.exception.TransactionNotFoundException;
import com.bank.exception.base.InternalServerErrorException;
import com.bank.exception.base.InvalidArgumentException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTests {
    @Mock
    private TransactionService transactionService;
    @InjectMocks
    private TransactionController transactionController;
    private CreateTransactionRequest createTransactionRequest;
    private com.bank.domain.model.Transaction domainTransaction;

    private long transactionId;

    @BeforeEach
    void setUp() {
        // Initialize test data
        domainTransaction = TestUtil.randomDomainTransaction();
        transactionId = domainTransaction.getId();

        createTransactionRequest = new CreateTransactionRequest();
        createTransactionRequest.setType(TransactionType.TRANSFER);
        createTransactionRequest.setAmount(domainTransaction.getAmount().toPlainString());
        createTransactionRequest.setFromAccountId(transactionId);
        createTransactionRequest.setToAccountId(TestUtil.randomPositiveLong());
    }

    @Test
    void testCreateTransaction() throws InvalidArgumentException {
        when(transactionService.create(any(CreateTransactionParams.class))).thenReturn(domainTransaction);
        Transaction transaction = transactionController.create(createTransactionRequest);
        verifyReturnedTransaction(transaction);
        verify(transactionService, times(1)).create(any(CreateTransactionParams.class));
    }

    @Test
    void testSubmitTransaction() throws InvalidArgumentException, InternalServerErrorException {
        when(transactionService.submit(anyLong())).thenReturn(domainTransaction);
        verifyReturnedTransaction(transactionController.submit(transactionId));
        verify(transactionService, times(1)).submit(transactionId);
    }

    @Test
    void testGetTransactionById() throws TransactionNotFoundException {
        when(transactionService.getById(transactionId)).thenReturn(Optional.of(domainTransaction));
        verifyReturnedTransaction(transactionController.getById(transactionId));
        verify(transactionService, times(1)).getById(transactionId);
    }

    @Test
    void testGetTransactionByIdNotFound() {
        when(transactionService.getById(transactionId)).thenReturn(Optional.empty());
        assertThrows(TransactionNotFoundException.class, () -> transactionController.getById(transactionId));
        verify(transactionService, times(1)).getById(transactionId);
    }

    private void verifyReturnedTransaction(Transaction transaction) {
        assertNotNull(transaction);
        assertEquals(transactionId, transaction.getId());
        assertEquals(domainTransaction.getType().getValue(), transaction.getType());
        assertEquals(domainTransaction.getStatus().getValue(), transaction.getStatus());
        assertEquals(domainTransaction.getAmount().toPlainString(), transaction.getAmount());
        assertEquals(domainTransaction.getFromAccountId(), transaction.getFromAccountId());
        assertEquals(domainTransaction.getToAccountId(), transaction.getToAccountId());
        assertEquals(domainTransaction.getCreatedAt().getEpochSecond(), transaction.getCreatedAt());
        assertEquals(domainTransaction.getCompletedAt().getEpochSecond(), transaction.getCompletedAt());
    }
}