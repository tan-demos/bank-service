package com.bank.domain.model;

public enum TransactionType {
    TRANSFER(0),
    DEPOSIT(1),
    WITHDRAWAL(2);

    private final Integer value;

    private TransactionType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

    public String toString() {
        return String.valueOf(this.value);
    }

    public static TransactionType fromValue(Integer value) {
        for (TransactionType v : values()) {
            if (v.value.equals(value)) {
                return v;
            }
        }
        throw new IllegalArgumentException(String.format("Unexpected value %d", value));
    }
}
