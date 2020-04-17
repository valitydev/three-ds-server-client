package com.rbkmoney.three.ds.server.client.utils.encoder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Encoder {

    public static String encode(String str) {
        return new String(Base64.getEncoder().encode(str.getBytes()));
    }

}
