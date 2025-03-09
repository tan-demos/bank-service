package com.bank.config;

import com.bank.domain.model.Account;
import com.bank.domain.model.Transaction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisBeanConfig {
    @Bean
    public RedisTemplate<String, Transaction> redisTemplateForTransaction(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Transaction> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key serializer (String)
        template.setKeySerializer(new StringRedisSerializer());

        // Value serializer (JSON)
        Jackson2JsonRedisSerializer<Transaction> serializer = new Jackson2JsonRedisSerializer<>(Transaction.class);
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }

    @Bean
    public RedisTemplate<String, Account> redisTemplateForAccount(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Account> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // Key serializer (String)
        template.setKeySerializer(new StringRedisSerializer());

        // Value serializer (JSON)
        Jackson2JsonRedisSerializer<Account> serializer = new Jackson2JsonRedisSerializer<>(Account.class);
        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }
}
