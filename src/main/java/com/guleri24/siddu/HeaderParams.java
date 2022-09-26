package com.guleri24.siddu;

/**
 * Contains constants representing the Siddu header parameter names.
 */
public class HeaderParams {
    private HeaderParams() {
    }

    /**
     * The algorithm used to sign a Siddu.
     */
    public static final String ALGORITHM = "alg";

    /**
     * The content type of the Siddu.
     */
    public static final String CONTENT_TYPE = "cty";

    /**
     * The media type of the Siddu.
     */
    public static final String TYPE = "typ";

    /**
     * The key ID of a Siddu used to specify the key for signature validation.
     */
    public static final String KEY_ID = "kid";
}
