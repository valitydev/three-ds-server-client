package com.rbkmoney.three.ds.server.client.constant;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum MessageType implements Valuable {

    AUTHENTICATION_REQUEST_MESSAGE("AReq"),
    AUTHENTICATION_RESPONSE_MESSAGE("ARes"),

    PREPARATION_REQUEST_MESSAGE("PReq"),
    PREPARATION_RESPONSE_MESSAGE("PRes"),

    RESULTS_REQUEST_MESSAGE("RReq"),
    RESULTS_RESPONSE_MESSAGE("RRes"),

    PROPRIETARY_AUTHENTICATION_REQUEST_MESSAGE("pArq"),
    PROPRIETARY_AUTHENTICATION_RESPONSE_MESSAGE("pArs"),

    PROPRIETARY_GET_CHALLENGE_REQUEST_MESSAGE("pGcq"),
    PROPRIETARY_GET_CHALLENGE_RESPONSE_MESSAGE("pGcs"),

    PROPRIETARY_PREPARATION_REQUEST_MESSAGE("pPrq"),
    PROPRIETARY_PREPARATION_RESPONSE_MESSAGE("pPrs"),

    CHALLENGE_REQUEST_MESSAGE("CReq"),
    CHALLENGE_RESPONSE_MESSAGE("CRes"),

    ERROR_RESPONSE("Erro");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    public static MessageType findByType(String messageType) {
        return Arrays.stream(MessageType.values())
                .filter(e -> e.getValue().equalsIgnoreCase(messageType))
                .findAny()
                .orElseThrow(() -> new IllegalStateException(String.format("Unsupported messageType %s.", messageType)));
    }

}
