package com.bank.event;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventHandler {
    @Async("transactionEventTaskExecutor") // if not specified, SimpleTaskExecutor will be used by default
    @EventListener
    public void handleEvent(TransactionEvent event) {
        System.out.println("Received event: " + event.getSource());
    }
}
