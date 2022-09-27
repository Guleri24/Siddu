package com.guleri24.siddu.exceptions;

public class TokenRevocationException extends SidduVerificationException {

    public TokenRevocationException(String message) {
        super(message);
    }

    public TokenRevocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
