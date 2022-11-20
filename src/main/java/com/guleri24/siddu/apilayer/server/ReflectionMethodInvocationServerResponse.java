package com.guleri24.siddu.apilayer.server;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Record representing the response for a {@link ReflectionMethodInvocationServerRequest} instance.
 */
record ReflectionMethodInvocationServerResponse(Serializable response) implements Serializable {

    /**
     * Constructor for the {@link ReflectionMethodInvocationServerResponse} class.
     *
     * @param response The response as a {@link Serializable}.
     */
    ReflectionMethodInvocationServerResponse(@NotNull Serializable response) {
        this.response = response;
    }

    /**
     * Getter for the response as a {@link Serializable}.
     *
     * @return The {@link Serializable}.
     */
    @Override
    public Serializable response() {
        return response;
    }
}
