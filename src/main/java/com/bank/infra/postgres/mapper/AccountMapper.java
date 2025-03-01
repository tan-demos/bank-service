package com.bank.infra.postgres.mapper;

import com.bank.domain.model.Account;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.Optional;

@Mapper
public interface AccountMapper {
    @Select("SELECT id, balance FROM account WHERE id=#{id}")
    Optional<Account> getById(@Param("id") long id);

    @Insert("INSERT INTO account(id, balance) VALUES(#{id}, #{balance})")
    void insert(Account account);

    @Select("SELECT balance FROM account WHERE id=#{id} FOR UPDATE")
    Optional<BigDecimal> getBalanceForUpdate(long id);

    @Update("UPDATE account SET balance=balance+#{change} WHERE id=#{id}")
    int changeBalance(long id, BigDecimal change);
}
