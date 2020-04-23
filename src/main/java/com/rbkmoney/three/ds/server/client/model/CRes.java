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
public class CRes {

    @ToString.Include
    private String messageType;

    @ToString.Include
    private String messageVersion;

    @ToString.Include
    private String htmlCreq;

    @ToString.Include
    private String threeDSServerTransID;

    @ToString.Include
    private String acsCounterAtoS;

    @ToString.Include
    private String acsTransID;

    @ToString.Include
    private String acsHTML;

    @ToString.Include
    private String acsUiType;

    @ToString.Include
    private String challengeCompletionInd;

    @ToString.Include
    private String challengeInfoHeader;

    @ToString.Include
    private String challengeInfoLabel;

    @ToString.Include
    private String challengeInfoText;

    @ToString.Include
    private String challengeInfoTextIndicator;

    @ToString.Include
    private String challengeSelectInfo;

    @ToString.Include
    private String expandInfoLabel;

    @ToString.Include
    private String expandInfoText;

    @ToString.Include
    private String issuerImage;

    @ToString.Include
    private List<MessageExtension> messageExtension;

    @ToString.Include
    private String oobAppURL;

    @ToString.Include
    private String obAppLabel;

    @ToString.Include
    private String oobContinueLabel;

    @ToString.Include
    private String psImage;

    @ToString.Include
    private String resendInformationLabel;

    @ToString.Include
    private String sdkTransID;

    @ToString.Include
    private String submitAuthenticationLabel;

    @ToString.Include
    private String whitelistingInfoText;

    @ToString.Include
    private String whyInfoLabel;

    @ToString.Include
    private String whyInfoText;

}
