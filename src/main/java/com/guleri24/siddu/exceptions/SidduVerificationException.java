package com.guleri24.siddu.exceptions;

public class SidduVerificationException extends RuntimeException {
    public SidduVerificationException(String message) {
        this(message, null);
    }

    public SidduVerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
