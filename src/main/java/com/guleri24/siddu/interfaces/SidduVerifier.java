package com.guleri24.siddu.interfaces;

import com.guleri24.siddu.exceptions.SidduDecodeException;

public interface SidduVerifier {
    DecodedSiddu verify(String token) throws SidduDecodeException;
    DecodedSiddu verify(DecodedSiddu siddu) throws SidduVerificationException;
}
