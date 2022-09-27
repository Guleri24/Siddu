package com.guleri24.siddu.interfaces;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface Payload {
    String getIssuer();

    String getSubject();

    List<String> getAudience();

    Date getExpiresAt();

    default Instant getExpiresAtAsInstant() {
        return getExpiresAt() != null ? getExpiresAt().toInstant() : null;
    }

    Date getNotBefore();

    default Instant getNotBeforeAsInstant() {
        return getNotBefore() != null ? getNotBefore().toInstant() : null;
    }

    Date getIssuedAt();

    default Instant getIssuedAtAsInstant() {
        return getIssuedAt() != null ? getIssuedAt().toInstant() : null;
    }

    String getId();

    Claim getClaim(String name);

    Map<String, Claim> getClaims();
}
