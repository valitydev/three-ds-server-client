package com.rbkmoney.three.ds.server.client.utils.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.rbkmoney.threeds.server.domain.error.ErrorComponent;

import java.io.IOException;
import java.util.EnumSet;

public class ErrorComponentDeserializer extends StdDeserializer<ErrorComponent> {

    public ErrorComponentDeserializer() {
        super(ErrorComponent.class);
    }

    protected ErrorComponentDeserializer(Class<?> vc) {
        super(vc);
    }

    protected ErrorComponentDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected ErrorComponentDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    @Override
    public ErrorComponent deserialize(JsonParser jsonParser, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String value = node.asText();

        return EnumSet.allOf(ErrorComponent.class)
                .stream()
                .filter(e -> e.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Unsupported ErrorComponent %s.", value)));

    }
}
