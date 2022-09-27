package com.guleri24.siddu.interfaces;

public interface DecodedSiddu extends PayloadEncrypted, Header {
    String getToken();

    String getHeader();

    String getPayload();

    String getSignature();
}
