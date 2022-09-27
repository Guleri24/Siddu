package com.guleri24.siddu.interfaces;

public interface Header {
    String getAlgorithm();
    String getType();
    String getContentType();
    String getKeyId();
    Claim getHeaderClaim(String name);
}
