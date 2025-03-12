package com.bank.infra.kafka;

import com.bank.domain.model.Transaction;
import com.bank.domain.pubsub.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProducerImpl implements Producer {
    @Autowired
    private KafkaTemplate<String, Transaction> kafkaTemplate;

    public void send(String eventName, Transaction transaction) {
        kafkaTemplate.send(eventName, transaction);
    }

}
