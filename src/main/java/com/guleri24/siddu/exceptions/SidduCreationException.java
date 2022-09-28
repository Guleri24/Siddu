package com.guleri24.siddu.exceptions;

/**
 * The exception that is thrown when a Siddu cannot be created.
 */
public class SidduCreationException extends RuntimeException {
    public SidduCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
