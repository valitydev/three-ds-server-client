package com.rbkmoney.three.ds.server.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rbkmoney.threeds.server.domain.message.MessageType;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
@ToString(onlyExplicitlyIncluded = true)
public class CReq {

    @ToString.Include
    private String threeDSServerTransID;

    @ToString.Include
    private String acsTransID;

    @ToString.Include
    private String messageType = MessageType.CREQ.getValue();

    @ToString.Include
    private String messageVersion;

    // Optional
    @ToString.Include
    private String threeDSSessionData;

    @ToString.Include
    private String challengeWindowSize;

}
