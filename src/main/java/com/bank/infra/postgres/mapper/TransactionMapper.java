package com.bank.infra.postgres.mapper;

import com.bank.domain.model.TransactionStatus;
import com.bank.domain.model.Transaction;
import com.bank.domain.model.TransactionType;
import com.bank.infra.postgres.handler.TransactionStatusTypeHandler;
import com.bank.infra.postgres.handler.TransactionTypeTypeHandler;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.time.Instant;
import java.util.Optional;

@Mapper
public interface TransactionMapper {

    // By default, EnumTypeHandler is used for enum type which passes string instead of ordinal int value.
    // EnumOrdinalTypeHandler is used if enum type doesn't define custom value, TransactionStatus and TransactionType can't use EnumOrdinalTypeHandler.
    @Select("INSERT INTO tx(type, from_account_id, to_account_id, amount, status, created_at, completed_at) " +
            "VALUES(#{type, typeHandler=com.bank.infra.postgres.handler.TransactionTypeTypeHandler}, #{fromAccountId}, #{toAccountId}, #{amount}, #{status, typeHandler=com.bank.infra.postgres.handler.TransactionStatusTypeHandler}, #{createdAt}, #{completedAt}) " +
            "RETURNING id")
    Long insert(Transaction transaction);

    @Select("SELECT id, type, from_account_id, to_account_id, amount, status, created_at, completed_at FROM tx WHERE id=#{id}")
    @Results({
            @Result(property = "status", column = "status", jdbcType = JdbcType.BIGINT, javaType = TransactionStatus.class, typeHandler = TransactionStatusTypeHandler.class),
            @Result(property = "type", column = "type", jdbcType = JdbcType.BIGINT, javaType = TransactionType.class, typeHandler = TransactionTypeTypeHandler.class)
    })
    Optional<Transaction> getById(long id);

    @Update("UPDATE tx SET status=#{newStatus, typeHandler=com.bank.infra.postgres.handler.TransactionStatusTypeHandler}, completed_at=#{completedAt} WHERE id=#{id} AND status=#{oldStatus, typeHandler=com.bank.infra.postgres.handler.TransactionStatusTypeHandler}")
    int changeStatusAndCompletedAt(long id, TransactionStatus oldStatus, TransactionStatus newStatus, Instant completedAt);
}
