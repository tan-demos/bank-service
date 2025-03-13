package com.bank.config;

import com.bank.api.model.Transaction;
import com.bank.util.ObjectPool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectPoolConfig {
    // To avoid allocating new objects
    @Bean
    public ObjectPool<Transaction> apiTransactionPool() {
        return new ObjectPool<>(Transaction::new);
    }
}
