package com.bank.domain.model;

public enum TransactionStatus {
    PENDING(0),
    SUCCEEDED(1),
    FAILED(2);

    private final Integer value;

    private TransactionStatus(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

    public String toString() {
        return String.valueOf(this.value);
    }

    public static TransactionStatus fromValue(Integer value) {
        for (TransactionStatus v : values()) {
            if (v.value.equals(value)) {
                return v;
            }
        }
        throw new IllegalArgumentException(String.format("Unexpected value %d", value));
    }
}