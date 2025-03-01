package com.bank.infra.postgres.mapper;

import com.bank.infra.postgres.entity.Transaction;
import org.apache.ibatis.annotations.*;

import java.sql.Timestamp;
import java.util.Optional;

@Mapper
public interface TransactionMapper {
    @Select("INSERT INTO tx(type, from_account_id, to_account_id, amount, status, created_at, completed_at) " +
            "VALUES(#{type}, #{fromAccountId}, #{toAccountId}, #{amount}, #{status}, #{createdAt}, #{completedAt}) " +
            "RETURNING id")
    Long insert(Transaction transaction);

    @Select("SELECT id, type, from_account_id, to_account_id, amount, status, created_at, completed_at FROM tx WHERE id=#{id}")
    Optional<Transaction> getById(long id);

    @Update("UPDATE tx SET status=#{newStatus}, completed_at=#{completedAt} WHERE id=#{id} AND status=#{oldStatus}")
    int changeStatusAndCompletedAt(long id, int oldStatus, int newStatus, Timestamp completedAt);
}
