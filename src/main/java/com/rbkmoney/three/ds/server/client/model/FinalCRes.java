package com.rbkmoney.three.ds.server.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rbkmoney.threeds.server.domain.message.MessageExtension;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatus;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;
import com.rbkmoney.threeds.server.serialization.deserializer.TransactionStatusDeserializer;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
@ToString(onlyExplicitlyIncluded = true)
public class FinalCRes {

    @ToString.Include
    private String messageType;

    @ToString.Include
    private String messageVersion;

    @ToString.Include
    private String threeDSServerTransID;

    @ToString.Include
    private String acsCounterAtoS;

    @ToString.Include
    private String acsTransID;

    @ToString.Include
    private String challengeCompletionInd;

    @ToString.Include
    private List<MessageExtension> messageExtension;

    @ToString.Include
    private String sdkTransID;

    @ToString.Include
    @JsonDeserialize(using = TransactionStatusDeserializer.class)
    private EnumWrapper<TransactionStatus> transStatus;

}
