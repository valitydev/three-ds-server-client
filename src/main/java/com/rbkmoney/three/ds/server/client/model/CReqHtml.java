package com.rbkmoney.three.ds.server.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rbkmoney.threeds.server.domain.message.MessageExtension;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
@ToString(onlyExplicitlyIncluded = true)
public class CReqHtml {

    @ToString.Include
    private String messageType;

    @ToString.Include
    private String messageVersion;

    @ToString.Include
    private String threeDSServerTransID;

    @ToString.Include
    private String acsCounterAtos;

    @ToString.Include
    private String acsTransID;

    @ToString.Include
    private String challengeCompletionInd;

    @ToString.Include
    private String sdkTransID;

    @ToString.Include
    private String transStatus;

    @ToString.Include
    private List<MessageExtension> messageExtension;

}
