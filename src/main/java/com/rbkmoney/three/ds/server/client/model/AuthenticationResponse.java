package com.rbkmoney.three.ds.server.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rbkmoney.threeds.server.domain.acs.AcsChallengeMandated;
import com.rbkmoney.threeds.server.domain.acs.AcsDecConInd;
import com.rbkmoney.threeds.server.domain.acs.AcsRenderingTypeWrapper;
import com.rbkmoney.threeds.server.domain.authentication.AuthenticationType;
import com.rbkmoney.threeds.server.domain.message.MessageExtension;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatus;
import com.rbkmoney.threeds.server.domain.transaction.TransactionStatusReason;
import com.rbkmoney.threeds.server.domain.whitelist.WhiteListStatus;
import com.rbkmoney.threeds.server.domain.whitelist.WhiteListStatusSource;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
@ToString(onlyExplicitlyIncluded = true)
public class AuthenticationResponse {

    @JsonUnwrapped(prefix = "message")
    private MessageData message;

    private String threeDSServerTransID;

    private String acsOperatorID;
    private String acsReferenceNumber;
    private AcsRenderingTypeWrapper acsRenderingType;
    private String acsSignedContent;
    private String acsTransID;
    private String acsURL;
    private String authenticationValue;

    @JsonProperty("broadInfo")
    private Object broadcastInformation;

    private String cardholderInfo;
    private String dsReferenceNumber;
    private String dsTransID;
    private String eci;
    private String sdkTransID;

    @JsonDeserialize(using = com.rbkmoney.three.ds.server.client.utils.deserializer.AcsChallengeMandatedDeserializer.class)
    private AcsChallengeMandated acsChallengeMandated;

    @JsonDeserialize(using = com.rbkmoney.three.ds.server.client.utils.deserializer.AcsDecConIndDeserializer.class)
    private AcsDecConInd acsDecConInd;

    @JsonDeserialize(using = com.rbkmoney.three.ds.server.client.utils.deserializer.AuthenticationTypeDeserializer.class)
    private AuthenticationType authenticationType;

    @JsonDeserialize(using = com.rbkmoney.three.ds.server.client.utils.deserializer.MessageExtensionDeserializer.class)
    private List<MessageExtension> messageExtension;

    @JsonDeserialize(using = com.rbkmoney.three.ds.server.client.utils.deserializer.TransactionStatusDeserializer.class)
    private TransactionStatus transStatus;

    @JsonDeserialize(using = com.rbkmoney.three.ds.server.client.utils.deserializer.TransactionStatusReasonDeserializer.class)
    private TransactionStatusReason transStatusReason;

    @JsonDeserialize(using = com.rbkmoney.three.ds.server.client.utils.deserializer.WhiteListStatusDeserializer.class)
    private WhiteListStatus whiteListStatus;

    @JsonDeserialize(using = com.rbkmoney.three.ds.server.client.utils.deserializer.WhiteListStatusSourceDeserializer.class)
    private WhiteListStatusSource whiteListStatusSource;

    @JsonUnwrapped
    @JsonProperty("Erro")
    private Error error;

}
