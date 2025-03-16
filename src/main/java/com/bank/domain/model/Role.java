package com.bank.domain.model;


public enum Role {
    GUEST(0),
    CUSTOMER(1),

    OPERATION(2),

    ADMIN(3);

    private final Integer value;

    private Role(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

    public String toString() {
        return String.valueOf(this.value);
    }

    public static Role fromValue(Integer value) {
        for (Role v : values()) {
            if (v.value.equals(value)) {
                return v;
            }
        }
        throw new IllegalArgumentException(String.format("Unexpected value %d", value));
    }
}