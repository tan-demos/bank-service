package com.bank.domain.service.impl;

import com.bank.TestUtil;
import com.bank.domain.model.Account;
import com.bank.domain.repository.AccountRepository;
import com.bank.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTests {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void testAddAccount() {
        Account account = TestUtil.randomDomainAccount();
        Account result = accountService.add(account.getId(), account.getBalance());
        assertNotNull(result);
        assertEquals(account, result);
        verify(accountRepository, times(1)).insert(any(Account.class));
    }

    @Test
    void testAddAccountWithNegativeBalance() {
        assertThrows(BadRequestException.class, () -> accountService.add(TestUtil.randomPositiveLong(), TestUtil.randomNegativeDecimal()));
        verify(accountRepository, never()).insert(any(Account.class));
    }

    @Test
    void testGetAccountById() {
        Account account = TestUtil.randomDomainAccount();
        when(accountRepository.getById(account.getId())).thenReturn(Optional.of(account));

        Optional<Account> optionalAccount = accountService.getById(account.getId());

        assertTrue(optionalAccount.isPresent());
        assertEquals(account, optionalAccount.get());
        verify(accountRepository, times(1)).getById(account.getId());
    }

    @Test
    void testGetAccountByIdNotFound() {
        var id = TestUtil.randomPositiveLong();
        when(accountRepository.getById(id)).thenReturn(Optional.empty());

        Optional<Account> optionalAccount = accountService.getById(id);

        assertTrue(optionalAccount.isEmpty());
        verify(accountRepository, times(1)).getById(id);
    }
}