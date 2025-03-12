package com.bank.config;

import com.bank.domain.pubsub.Producer;
import com.bank.infra.kafka.ProducerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventConfig {
    @Bean
    public Producer producer() {
        return new ProducerImpl();
    }
}
