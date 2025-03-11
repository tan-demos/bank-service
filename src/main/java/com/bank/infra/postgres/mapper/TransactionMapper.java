package com.bank.infra.postgres.mapper;

import com.bank.domain.model.TransactionStatus;
import com.bank.domain.model.Transaction;
import org.apache.ibatis.annotations.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Mapper
public interface TransactionMapper {

    // By default, EnumTypeHandler is used for enum type which passes string instead of ordinal int value.
    // EnumOrdinalTypeHandler is used if enum type doesn't define custom value, TransactionStatus and TransactionType can't use EnumOrdinalTypeHandler.
    @Select("""
            INSERT INTO tx(type, from_account_id, to_account_id, amount, status, created_at, completed_at)
            VALUES(#{type}, #{fromAccountId}, #{toAccountId}, #{amount}, #{status}, #{createdAt}, #{completedAt})
            RETURNING id
            """)
    Long insert(Transaction transaction);

    @Select("""
                SELECT id, type, from_account_id, to_account_id, amount, status, created_at, completed_at
                FROM tx
                WHERE id=#{id}
            """)
    @Results({
            @Result(property = "fromAccountId", column = "from_account_id"),
            @Result(property = "toAccountId", column = "to_account_id"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "completedAt", column = "completed_at")
    })
    Optional<Transaction> getById(long id);

    @Update("""
            UPDATE tx 
            SET status=#{newStatus}, completed_at=#{completedAt} 
            WHERE id=#{id} AND status=#{oldStatus}
            """)
    int changeStatusAndCompletedAt(long id, TransactionStatus oldStatus, TransactionStatus newStatus, Instant completedAt);


    /*
    * This is a hybrid approach to do pagination, instead of scan all rows until the offset
    * this way only scan id (which is a primary index) until offset, it's more efficient
    * */
    @Select("""
            SELECT id, type, from_account_id, to_account_id, amount, status, created_at, completed_at
                FROM tx
                WHERE id > (
                    SELECT id FROM tx ORDER BY id ASC LIMIT 1 OFFSET #{offset}
                )
                ORDER BY id ASC
                LIMIT #{limit}
            """)
    @Results({
            @Result(property = "fromAccountId", column = "from_account_id"),
            @Result(property = "toAccountId", column = "to_account_id"),
            @Result(property = "createdAt", column = "created_at"),
            @Result(property = "completedAt", column = "completed_at")
    })
    List<Transaction> list(int offset, int limit);

    @Select("SELECT COUNT(*) FROM tx")
    int count();
}
