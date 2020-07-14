package com.rbkmoney.three.ds.server.client.utils.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.rbkmoney.threeds.server.domain.authentication.AuthenticationType;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatus;

import java.io.IOException;
import java.util.EnumSet;

public class AuthenticationTypeDeserializer extends StdDeserializer<AuthenticationType> {

    public AuthenticationTypeDeserializer() {
        super(TransactionStatus.class);
    }

    protected AuthenticationTypeDeserializer(Class<?> vc) {
        super(vc);
    }

    protected AuthenticationTypeDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected AuthenticationTypeDeserializer(StdDeserializer<?> src) {
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
                .orElseThrow(() -> new IllegalStateException(String.format("Unsupported AuthenticationType %s.", value)));

    }
}
