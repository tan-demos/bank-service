package com.bank.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionEventHandler {
    @EventListener
    public void handleEvent(TransactionEvent event) {
        System.out.println("Received event: " + event.getSource());
    }
}
