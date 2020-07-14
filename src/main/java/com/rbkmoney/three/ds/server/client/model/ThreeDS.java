package com.rbkmoney.three.ds.server.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rbkmoney.threeds.server.domain.threedsrequestor.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
@ToString(onlyExplicitlyIncluded = true)
public class ThreeDS {

    @JsonProperty("CompInd")
    private ThreeDsMethodCompletionIndicator compInd;

    @JsonProperty("RequestorAuthenticationInd")
    private ThreeDSRequestorAuthenticationInd requestorAuthenticationInd;

    @JsonProperty("RequestorAuthenticationInfo")
    private ThreeDSRequestorAuthenticationInfo requestorAuthenticationInfo;

    @JsonProperty("ReqAuthMethodInd")
    private ThreeDSReqAuthMethodInd reqAuthMethodInd;

    @JsonProperty("RequestorChallengeInd")
    private ThreeDSRequestorChallengeInd requestorChallengeInd;

    @JsonProperty("RequestorDecMaxTime")
    private String requestorDecMaxTime;

    @JsonProperty("RequestorDecReqInd")
    private ThreeDSRequestorDecReqInd requestorDecReqInd;

    @JsonProperty("RequestorID")
    private String requestorID;

    @JsonProperty("RequestorName")
    private String requestorName;

    @JsonProperty("RequestorPriorAuthenticationInfo")
    private ThreeDSRequestorPriorAuthenticationInfo requestorPriorAuthenticationInfo;

    @JsonProperty("RequestorURL")
    private String requestorURL;

    @JsonProperty("ServerRefNumber")
    private String serverRefNumber;

    @JsonProperty("ServerOperatorID")
    private String serverOperatorID;

    @JsonProperty("ServerTransID")
    private String serverTransID;

    @JsonProperty("ServerURL")
    private String serverURL;

}
