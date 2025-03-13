package com.bank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync // without this, @Async on method will be ignored
public class AsyncConfig {
    @Bean(name = "transactionEventTaskExecutor") // Optional: name the bean if you have multiple executors
    public Executor transactionEventTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // Thread pool settings
        executor.setCorePoolSize(2);        // Minimum number of threads
        executor.setMaxPoolSize(10);        // Maximum number of threads
        executor.setQueueCapacity(100);     // Queue size for pending tasks
        executor.setThreadNamePrefix("TransactionEvent-"); // Thread name prefix for debugging

        // Optional: Customize behavior when queue is full
        executor.setRejectedExecutionHandler(new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy());

        executor.initialize();
        return executor;
    }
}