package com.rbkmoney.three.ds.server.client.utils.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatus;

import java.io.IOException;
import java.util.EnumSet;

public class TransactionStatusDeserializer extends StdDeserializer<TransactionStatus> {

    public TransactionStatusDeserializer() {
        super(TransactionStatus.class);
    }

    protected TransactionStatusDeserializer(Class<?> vc) {
        super(vc);
    }

    protected TransactionStatusDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected TransactionStatusDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    @Override
    public TransactionStatus deserialize(JsonParser jsonParser, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String value = node.asText();

        return EnumSet.allOf(TransactionStatus.class)
                .stream()
                .filter(e -> e.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Unsupported transStatus %s.", value)));

    }
}
