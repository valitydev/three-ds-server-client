package com.rbkmoney.three.ds.server.client.utils.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.rbkmoney.three.ds.server.client.constant.MessageType;

import java.io.IOException;

public class MessageTypeDeserializer extends StdDeserializer<MessageType> {

    public MessageTypeDeserializer() {
        super(MessageType.class);
    }

    protected MessageTypeDeserializer(Class<?> vc) {
        super(vc);
    }

    protected MessageTypeDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected MessageTypeDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    @Override
    public MessageType deserialize(JsonParser jsonParser, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String value = node.asText();
        return MessageType.findByType(value);

    }
}
