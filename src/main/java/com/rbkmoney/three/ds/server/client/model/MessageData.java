package com.rbkmoney.three.ds.server.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rbkmoney.three.ds.server.client.constant.MessageType;
import com.rbkmoney.three.ds.server.client.utils.deserializer.MessageTypeDeserializer;
import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.message.MessageExtension;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
@ToString(onlyExplicitlyIncluded = true)
public class MessageData {

    @JsonProperty("Version")
    private String version;

    @JsonProperty("Type")
    @JsonDeserialize(using = MessageTypeDeserializer.class)
    private MessageType type;

    @JsonProperty("Category")
    private MessageCategory category;

    @JsonProperty("Extension")
    private List<MessageExtension> extension;
}
