package com.rbkmoney.three.ds.server.client.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonField {

    THREE_DS_SERVER_TRANS_ID("threeDSServerTransID"),
    ACS_TRANS_ID("acsTransID"),
    ECI("eci"),
    AUTHENTICATION_VALUE("authenticationValue"), // CAVV (Cardholder Authentication Verification Value)
    MESSAGE_TYPE("messageType"),
    MESSAGE_VERSION("messageVersion"),
    THREE_DS_SESSION_DATA("threeDSSessionData"),
    CHALLENGE_WINDOW_SIZE("challengeWindowSize"),
    THREE_DS_REQUESTOR_NAME("threeDSRequestorName"),
    THREE_DS_REQUESTOR_URL("threeDSRequestorURL"),
    TRANS_STATUS("transStatus"),
    ACS_CHALLENGE_MANDATED("acsChallengeMandated");

    private final String value;

}
