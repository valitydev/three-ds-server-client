package com.rbkmoney.three.ds.server.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rbkmoney.threeds.server.domain.message.MessageExtension;
import com.rbkmoney.threeds.server.domain.message.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
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

    @ToString.Include
    private String challengeWindowSize;

    // Optional fields
    @ToString.Include
    private String threeDSSessionData;

    @ToString.Include
    private String threeDSRequestorAppURL;

    @ToString.Include
    private String challengeCancel;

    @ToString.Include
    private String challengeDataEntry;

    @ToString.Include
    private String challengeHTMLDataEntry;

    @ToString.Include
    private String challengeNoEntry;

    @ToString.Include
    private List<MessageExtension> messageExtension;

    @ToString.Include
    private String oobContinue;

    @ToString.Include
    private String resendChallenge;

    @ToString.Include
    private String sdkTransID;

    @ToString.Include
    private String sdkCounterStoA;

    @ToString.Include
    private String whitelistingDataEntry;

}
