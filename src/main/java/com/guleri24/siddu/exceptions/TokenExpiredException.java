package com.guleri24.siddu.exceptions;

import java.io.Serial;
import java.time.Instant;

public class TokenExpiredException extends SidduVerificationException {
    @Serial
    private static final long serialVersionUID = -7076928975713577708L;

    private final Instant expiredOn;

    public TokenExpiredException(String message, Instant expiredOn) {
        super(message);
        this.expiredOn = expiredOn;
    }

    public Instant getExpiredOn() {
        return expiredOn;
    }
}
