package com.rbkmoney.three.ds.server.client.utils.decoder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Decoder {

    public static String decode(String str) {
        return new String(Base64.getDecoder().decode(str.getBytes()), StandardCharsets.UTF_8);
    }

}
