package com.bank.infra.redis;

import com.bank.domain.model.Page;
import com.bank.domain.model.Transaction;
import com.bank.domain.model.TransactionStatus;
import com.bank.domain.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(CachedTransactionRepository.class);
    private final TransactionRepository delegate;
    private final RedisTemplate<String, Transaction> redisTemplate;
    private final int cacheTtlMinutes;
    @Autowired
    public CachedTransactionRepository(@Qualifier("defaultTransactionRepository") TransactionRepository delegate,
                                   RedisTemplate<String, Transaction> redisTemplate,
                                   @Value("${com.bank.cache.redis.ttl-minutes}") int cacheTtlMinutes) {
        this.delegate = delegate;
        this.redisTemplate = redisTemplate;
        this.cacheTtlMinutes = cacheTtlMinutes;

        logger.info("Cache TTL: {} minutes", this.cacheTtlMinutes);
    }

    @Override
    public void insert(Transaction transaction) {
        delegate.insert(transaction);
        redisTemplate.delete(getCacheKey(transaction.getId()));
    }

    @Override
    public Optional<Transaction> getById(long id) {
        var key = getCacheKey(id);
        var transaction = redisTemplate.opsForValue().get(key);
        if (transaction == null) {
            logger.info("Transaction not found in cache");
            var optionalTransaction = delegate.getById(id);
            if (optionalTransaction.isPresent()) {
                transaction = optionalTransaction.get();
                redisTemplate.opsForValue().set(key, transaction, Duration.ofMinutes(cacheTtlMinutes));
            }
        } else {
            logger.info("Transaction found in cache");
        }
        if (transaction != null) {
            logger.info("Transaction:id={}, from={}, to={}, amount={}", transaction.getId(),
                    transaction.getFromAccountId(),
                    transaction.getToAccountId(),
                    transaction.getAmount());
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
