package com.guleri24.siddu.exceptions;

import com.guleri24.siddu.crypto.Algorithm;

/**
 * The exception that is thrown when signature is not able to be generated.
 */
public class SignatureGenerationException extends SidduCreationException {
    public SignatureGenerationException(Algorithm algorithm, Throwable cause) {
        super("The Token's Signature couldn't be generated when signing using the Algorithm: " + algorithm, cause);
    }
}
