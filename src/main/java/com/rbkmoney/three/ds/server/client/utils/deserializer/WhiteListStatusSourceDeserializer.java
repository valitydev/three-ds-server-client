package com.rbkmoney.three.ds.server.client.utils.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.rbkmoney.threeds.server.domain.whitelist.WhiteListStatusSource;

import java.io.IOException;
import java.util.EnumSet;

public class WhiteListStatusSourceDeserializer extends StdDeserializer<WhiteListStatusSource> {

    public WhiteListStatusSourceDeserializer() {
        super(WhiteListStatusSource.class);
    }

    protected WhiteListStatusSourceDeserializer(Class<?> vc) {
        super(vc);
    }

    protected WhiteListStatusSourceDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected WhiteListStatusSourceDeserializer(StdDeserializer<?> src) {
        super(src);
    }


    @Override
    public WhiteListStatusSource deserialize(JsonParser jsonParser, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String value = node.asText();

        return EnumSet.allOf(WhiteListStatusSource.class)
                .stream()
                .filter(e -> e.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Unsupported value %s.", value)));

    }
}
