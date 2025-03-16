package com.bank.exception.base;

public class InvalidArgumentException extends Exception {
    public InvalidArgumentException() {
        super();
    }

    public InvalidArgumentException(String message) {
        super(message);
    }
}
