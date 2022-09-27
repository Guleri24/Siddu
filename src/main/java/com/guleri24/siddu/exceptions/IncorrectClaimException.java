package com.guleri24.siddu.exceptions;

import com.guleri24.siddu.interfaces.Claim;

public class IncorrectClaimException extends InvalidClaimException {
    private final String claimName;
    private final Claim claimValue;

    public IncorrectClaimException(String message, String claimName, Claim claim) {
        super(message);
        this.claimName = claimName;
        this.claimValue = claim;
    }

    public String getClaimName() {
        return claimName;
    }

    public Claim getClaimValue() {
        return claimValue;
    }
}
