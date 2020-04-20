package com.rbkmoney.three.ds.server.client.utils.decoder;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DecoderTest {

    @Test
    public void decodeTest() {
        String threeDSMethodData = "eyJ0aHJlZURTU2VydmVyVHJhbnNJRCI6IjNhYzdjYWE3LWFhNDItMjY2My03OTFiLTJhYzA1YTU0MmM0YSIsInRocmVlRFNNZXRob2ROb3RpZmljYXRpb25VUkwiOiJ0aHJlZURTTWV0aG9kTm90aWZpY2F0aW9uVVJMIn0=";
        String decode = Decoder.decode(threeDSMethodData);

        String expect = "{\"threeDSServerTransID\":\"3ac7caa7-aa42-2663-791b-2ac05a542c4a\",\"threeDSMethodNotificationURL\":\"threeDSMethodNotificationURL\"}";
        assertEquals(expect, decode);
    }

}