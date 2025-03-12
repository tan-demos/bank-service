package com.bank.event;

import org.springframework.context.ApplicationEvent;

public class TransactionEvent extends ApplicationEvent {
    public TransactionEvent(Long transactionId) {
        super(transactionId);
    }
}
