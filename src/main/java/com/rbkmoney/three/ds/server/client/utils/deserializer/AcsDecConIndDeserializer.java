package com.rbkmoney.three.ds.server.client.utils.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.rbkmoney.threeds.server.domain.authentication.AuthenticationType;

import java.io.IOException;
import java.util.EnumSet;

public class AcsDecConIndDeserializer extends StdDeserializer<AuthenticationType> {

    public AcsDecConIndDeserializer() {
        super(AuthenticationType.class);
    }

    protected AcsDecConIndDeserializer(Class<?> vc) {
        super(vc);
    }

    protected AcsDecConIndDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected AcsDecConIndDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    @Override
    public AuthenticationType deserialize(JsonParser jsonParser, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String value = node.asText();

        return EnumSet.allOf(AuthenticationType.class)
                .stream()
                .filter(e -> e.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Unsupported authenticationType %s.", value)));

    }
}
