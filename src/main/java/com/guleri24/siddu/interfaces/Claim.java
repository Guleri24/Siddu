package com.guleri24.siddu.interfaces;

import com.guleri24.siddu.exceptions.SidduDecodeException;

import java.time.Instant;
import java.util.Date;
import java.util.Map;

public interface Claim {
    boolean isNull();

    boolean isMissing();

    Boolean asBoolean();

    Integer asInteger();

    Long asLong();

    Double asDouble();

    String asString();

    Date asDate();

    default Instant asInstant() {
        Date date = asDate();
        return date != null ? date.toInstant() : null;
    }

    <T> T[] asArray(Class<T> tClass) throws SidduDecodeException;

    Map<String, Object> asMap() throws SidduDecodeException;

    <T> T as(Class<T> tClass) throws SidduDecodeException;
}
