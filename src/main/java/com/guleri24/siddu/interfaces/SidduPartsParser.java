package com.guleri24.siddu.interfaces;

import com.guleri24.siddu.exceptions.SidduDecodeException;

public interface SidduPartsParser {
    PayloadEncrypted parsePayload(String json) throws SidduDecodeException;

    Header parseHeader(String json) throws SidduDecodeException;
}
