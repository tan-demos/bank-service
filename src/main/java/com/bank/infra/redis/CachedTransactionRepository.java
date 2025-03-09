package com.bank.infra.redis;

import com.bank.domain.model.Page;
import com.bank.domain.model.Transaction;
import com.bank.domain.model.TransactionStatus;
import com.bank.domain.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;


@Repository("cachedTransactionRepository")
public class CachedTransactionRepository implements TransactionRepository {

    @Autowired
    @Qualifier("defaultTransactionRepository")
    private TransactionRepository delegate;

    @Autowired
    private RedisTemplate<String, Transaction> redisTemplate;

    @Value("${com.bank.cache.redis.ttl-minutes}")
    private int cacheTtlMinutes;

    @Override
    public void insert(Transaction transaction) {
        delegate.insert(transaction);
        redisTemplate.delete(String.valueOf(transaction.getId()));
    }

    @Override
    public Optional<Transaction> getById(long id) {
        var transaction = redisTemplate.opsForValue().get(String.valueOf(id));
        if (transaction == null) {
            var optionalAccount = delegate.getById(id);
            if (optionalAccount.isPresent()) {
                transaction = optionalAccount.get();
                redisTemplate.opsForValue().set(String.valueOf(id), transaction, Duration.ofMinutes(cacheTtlMinutes));
            }
        }
        return Optional.ofNullable(transaction);
    }

    @Override
    public boolean changeStatusAndCompletedAt(long id, TransactionStatus oldStatus, TransactionStatus newStatus, Instant completedAt) {
        var result = delegate.changeStatusAndCompletedAt(id, oldStatus, newStatus, completedAt);
        redisTemplate.delete(getCacheKey(id));
        return result;
    }

    private String getCacheKey(long id) {
        return String.format("tx:%d", id);
    }

    @Override
    public Page<Transaction> getPage(int page, int size) {
        return delegate.getPage(page, size);
    }
}
