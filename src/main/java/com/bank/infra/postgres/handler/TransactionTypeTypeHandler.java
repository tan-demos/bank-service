package com.bank.infra.postgres.handler;

import com.bank.domain.model.TransactionType;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@MappedTypes(TransactionType.class)
public class TransactionTypeTypeHandler implements TypeHandler<TransactionType> {
    @Override
    public void setParameter(PreparedStatement ps, int i, TransactionType parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            ps.setNull(i, JdbcType.TINYINT.TYPE_CODE);
        } else {
            ps.setInt(i, parameter.getValue());
        }
    }

    @Override
    public TransactionType getResult(ResultSet rs, String columnName) throws SQLException {
        int value = rs.getInt(columnName);
        return rs.wasNull() ? null : TransactionType.fromValue(value);
    }

    @Override
    public TransactionType getResult(ResultSet rs, int columnIndex) throws SQLException {
        int value = rs.getInt(columnIndex);
        return rs.wasNull() ? null : TransactionType.fromValue(value);
    }

    @Override
    public TransactionType getResult(CallableStatement cs, int columnIndex) throws SQLException {
        int value = cs.getInt(columnIndex);
        return cs.wasNull() ? null : TransactionType.fromValue(value);
    }
}
