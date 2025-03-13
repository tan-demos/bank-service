package com.bank.infra.kafka;

import com.bank.domain.model.Transaction;
import com.bank.event.TransactionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class Consumer {
    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);

    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public Consumer(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @KafkaListener(topics = "transaction-created", groupId = "bank-service")
    public void listen(Transaction transaction) {
        logger.info("Received kafka message: transaction id={}", transaction.getId());
        this.eventPublisher.publishEvent(new TransactionEvent(transaction.getId()));
    }

}
