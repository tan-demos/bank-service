package com.bank.config;

import com.bank.domain.service.AccountService;
import com.bank.domain.service.TransactionExecutor;
import com.bank.domain.service.TransactionService;
import com.bank.domain.service.impl.AccountServiceImpl;
import com.bank.domain.service.impl.TransactionExecutorImpl;
import com.bank.domain.service.impl.TransactionServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceBeanConfig {
    @Bean
    public AccountService accountService() {
        return new AccountServiceImpl();
    }

    @Bean
    public TransactionService transactionService() {
        return new TransactionServiceImpl();
    }

    @Bean
    public TransactionExecutor transactionExecutor() {
        return new TransactionExecutorImpl();
    }
}
