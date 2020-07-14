package com.rbkmoney.three.ds.server.client.utils.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.rbkmoney.threeds.server.domain.whitelist.WhiteListStatus;

import java.io.IOException;
import java.util.EnumSet;

public class WhiteListStatusDeserializer extends StdDeserializer<WhiteListStatus> {

    public WhiteListStatusDeserializer() {
        super(WhiteListStatus.class);
    }

    protected WhiteListStatusDeserializer(Class<?> vc) {
        super(vc);
    }

    protected WhiteListStatusDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected WhiteListStatusDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    @Override
    public WhiteListStatus deserialize(JsonParser jsonParser, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String value = node.asText();

        return EnumSet.allOf(WhiteListStatus.class)
                .stream()
                .filter(e -> e.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Unsupported whiteListStatus %s.", value)));

    }
}
