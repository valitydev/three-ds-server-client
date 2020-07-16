package com.rbkmoney.three.ds.server.client.utils.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.rbkmoney.threeds.server.domain.error.ErrorCode;

import java.io.IOException;
import java.util.EnumSet;

public class ErrorCodeDeserializer extends StdDeserializer<ErrorCode> {

    public ErrorCodeDeserializer() {
        super(ErrorCode.class);
    }

    protected ErrorCodeDeserializer(Class<?> vc) {
        super(vc);
    }

    protected ErrorCodeDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected ErrorCodeDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    @Override
    public ErrorCode deserialize(JsonParser jsonParser, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String value = node.asText();

        return EnumSet.allOf(ErrorCode.class)
                .stream()
                .filter(e -> e.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Unsupported ErrorCode %s.", value)));

    }
}