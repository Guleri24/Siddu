package com.guleri24.siddu.interfaces;

import java.util.Map;

public interface PayloadEncrypted extends Payload {

    Map<String, String> getEncryptedPayload();

    String getPayload();

}
