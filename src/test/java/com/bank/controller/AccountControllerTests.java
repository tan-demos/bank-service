package com.bank.controller;

import com.bank.TestUtil;
import com.bank.api.model.Account;
import com.bank.api.model.AddAccountRequest;
import com.bank.domain.service.AccountService;
import com.bank.exception.AccountNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTests {
    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    private AddAccountRequest addAccountRequest;
    private com.bank.domain.model.Account domainAccount;

    @BeforeEach
    void setUp() {
        // Initialize test data
        var accountId = TestUtil.randomPositiveLong();
        var balance = TestUtil.randomPositiveDecimal();
        addAccountRequest = new AddAccountRequest();
        addAccountRequest.setAccountId(accountId);
        addAccountRequest.setBalance(balance.toPlainString());

        domainAccount = com.bank.domain.model.Account.builder().build();
        domainAccount.setId(accountId);
        domainAccount.setBalance(balance);
    }

    @Test
    void testAddAccount() {
        when(accountService.add(anyLong(), any(BigDecimal.class))).thenReturn(domainAccount);

        Account account = accountController.add(addAccountRequest);

        assertNotNull(account);
        assertEquals(domainAccount.getId(), account.getId());
        assertEquals(domainAccount.getBalance().toPlainString(), account.getBalance());

        var balance = addAccountRequest.getBalance() != null ? new BigDecimal(addAccountRequest.getBalance()) : BigDecimal.ZERO;
        verify(accountService, times(1)).add(addAccountRequest.getAccountId(), balance);
    }

    @Test
    void testAddAccountWithNullBalance() {
        addAccountRequest.setBalance(null);
        domainAccount.setBalance(BigDecimal.ZERO);

        when(accountService.add(anyLong(), eq(BigDecimal.ZERO))).thenReturn(domainAccount);

        Account account = accountController.add(addAccountRequest);
        
        assertNotNull(account);
        assertEquals(domainAccount.getId(), account.getId());
        assertEquals(domainAccount.getBalance().toPlainString(), account.getBalance());

        verify(accountService, times(1)).add(domainAccount.getId(), BigDecimal.ZERO);
    }

    @Test
    void testGetAccount() {
        when(accountService.getById(domainAccount.getId())).thenReturn(Optional.of(domainAccount));

        Account account = accountController.get(domainAccount.getId());

        assertNotNull(account);
        assertEquals(domainAccount.getId(), account.getId());
        assertEquals(domainAccount.getBalance().toPlainString(), account.getBalance());

        verify(accountService, times(1)).getById(domainAccount.getId());
    }

    @Test
    void testGetAccountNotFound() {
        var accountId = TestUtil.randomPositiveLong();
        when(accountService.getById(accountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountController.get(accountId));

        verify(accountService, times(1)).getById(accountId);
    }
}