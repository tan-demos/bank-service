package com.bank.infra.redis;

import com.bank.domain.model.Account;
import com.bank.domain.model.Page;
import com.bank.domain.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;

@Repository("cachedAccountRepository")
public class CachedAccountRepository implements AccountRepository {


    @Autowired
    @Qualifier("defaultAccountRepository")
    private AccountRepository delegate;

    @Autowired
    private RedisTemplate<String, Account> redisTemplate;

    @Value("${com.bank.cache.redis.ttl-minutes}")
    private int cacheTtlMinutes;

    @Override
    public void insert(Account account) {
        delegate.insert(account);
        redisTemplate.delete(String.valueOf(account.getId()));
    }

    @Override
    public Optional<Account> getById(long id) {
        var key = getCacheKey(id);
        var account = redisTemplate.opsForValue().get(key);
        if (account == null) {
            var optionalAccount = delegate.getById(id);
            if (optionalAccount.isPresent()) {
                account = optionalAccount.get();
                redisTemplate.opsForValue().set(key, account, Duration.ofMinutes(cacheTtlMinutes));
            }
        }
        return Optional.ofNullable(account);
    }

    @Override
    public Optional<BigDecimal> getBalanceForUpdate(long id) {
        return delegate.getBalanceForUpdate(id);
    }

    @Override
    public int changeBalance(long id, BigDecimal change) {
        var result = delegate.changeBalance(id, change);
        redisTemplate.delete(getCacheKey(id));
        return result;
    }

    private String getCacheKey(long id) {
        return String.format("account:%d", id);
    }

    @Override
    public Page<Account> getPage(int page, int size) {
        return delegate.getPage(page, size);
    }
}
