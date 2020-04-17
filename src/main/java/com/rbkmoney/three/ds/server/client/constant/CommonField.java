package com.rbkmoney.three.ds.server.client.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonField {

    ID("x-ul-testcaserun-id"), // TODO: only for TEST DS
    THREE_DS_SERVER_TRANS_ID("threeDSServerTransID"),
    ACS_TRANS_ID("acsTransID"),
    MESSAGE_TYPE("messageType"),
    MESSAGE_VERSION("messageVersion"),
    THREE_DS_SESSION_DATA("threeDSSessionData"),
    CHALLENGE_WINDOW_SIZE("challengeWindowSize"),
    HTML_CREQ("htmlCreq"),
    THREE_DS_REQUESTOR_NAME("threeDSRequestorName"),
    THREE_DS_REQUESTOR_URL("threeDSRequestorURL");

    private final String value;

}
