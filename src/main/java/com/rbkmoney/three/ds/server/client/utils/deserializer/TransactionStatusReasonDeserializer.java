package com.rbkmoney.three.ds.server.client.utils.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatusReason;

import java.io.IOException;
import java.util.EnumSet;

public class TransactionStatusReasonDeserializer extends StdDeserializer<TransactionStatusReason> {

    public TransactionStatusReasonDeserializer() {
        super(TransactionStatusReason.class);
    }

    protected TransactionStatusReasonDeserializer(Class<?> vc) {
        super(vc);
    }

    protected TransactionStatusReasonDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected TransactionStatusReasonDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    @Override
    public TransactionStatusReason deserialize(JsonParser jsonParser, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String value = node.asText();

        return EnumSet.allOf(TransactionStatusReason.class)
                .stream()
                .filter(e -> e.getValue().equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Unsupported value %s.", value)));

    }
}
