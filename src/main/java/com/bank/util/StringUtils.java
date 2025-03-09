package com.bank.util;

public class StringUtils {
    public static<T extends String>  boolean isEmpty(T s) {
        return s == null || "".contentEquals(s);
    }
}
