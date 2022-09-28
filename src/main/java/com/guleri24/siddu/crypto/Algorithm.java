package com.guleri24.siddu.crypto;

@SuppressWarnings("WeakerAccess")
/**
 * The Algorithm class represents an algorithm to be used in the Signing or Verification process of a Token.
 * <p>
 * This class and its subclasses are thread-safe.
 */
public abstract class Algorithm {
    private final String name;
    private final String description;

//    public static Algorithm Argon() throws IllegalArgumentException {
//       return new Argon();
//    }

    protected Algorithm(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

