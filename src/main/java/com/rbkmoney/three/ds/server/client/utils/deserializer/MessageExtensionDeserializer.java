package com.rbkmoney.three.ds.server.client.utils.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.rbkmoney.threeds.server.domain.message.MessageExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessageExtensionDeserializer extends StdDeserializer<List<MessageExtension>> {

    public MessageExtensionDeserializer() {
        super(MessageExtension.class);
    }

    protected MessageExtensionDeserializer(Class<?> vc) {
        super(vc);
    }

    protected MessageExtensionDeserializer(JavaType valueType) {
        super(valueType);
    }

    protected MessageExtensionDeserializer(StdDeserializer<?> src) {
        super(src);
    }

    @Override
    public List<MessageExtension> deserialize(JsonParser jsonParser, DeserializationContext ctxt)
            throws IOException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);

        if (node.isArray()) {
            ArrayNode arrayNode = (ArrayNode) node;
            List<MessageExtension> validValues = new ArrayList<>();
            for (JsonNode jsonNode : arrayNode) {
                validValues.add(oc.treeToValue(jsonNode, MessageExtension.class));
            }
            return validValues;
        }

        return Collections.emptyList();
    }
}
