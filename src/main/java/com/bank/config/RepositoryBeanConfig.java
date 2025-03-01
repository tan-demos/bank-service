package com.bank.config;

import com.bank.infra.postgres.AccountRepositoryImpl;
import com.bank.infra.postgres.TransactionRepositoryImpl;
import com.bank.domain.repository.AccountRepository;
import com.bank.domain.repository.TransactionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryBeanConfig {
    @Bean
    public AccountRepository accountRepository() {
        return new AccountRepositoryImpl();
    }

    @Bean
    public TransactionRepository transactionRepository() {
        return new TransactionRepositoryImpl();
    }
}
