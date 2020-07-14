package com.rbkmoney.three.ds.server.client.utils.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.rbkmoney.threeds.server.domain.acs.AcsChallengeMandated;

import java.io.IOException;
import java.util.EnumSet;

public class AcsChallengeMandatedDeserializer extends StdDeserializer<AcsChallengeMandated> {

    public AcsChallengeMandatedDeserializer() {
        super(AcsChallengeMandated.class);
    }

    protected AcsChallengeMandatedDeserializer(Class<?> vc) {
        super(vc);
    }

    protected AcsChallengeMandatedDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected AcsChallengeMandatedDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    @Override
    public AcsChallengeMandated deserialize(JsonParser jsonParser, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String value = node.asText();

        return EnumSet.allOf(AcsChallengeMandated.class)
                .stream()
                .filter(e -> e.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Unsupported acsChallengeMandated %s.", value)));

    }
}
