package com.rbkmoney.three.ds.server.client.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Method {

    AUTHENTICATION("authentication"),
    RESULTS("results"),
    CHALLENGE("challenge"),
    PREPARATION("preparation"),

    PROPRIETARY_INFORMATION("Proprietary Information"),
    PROPRIETARY_OUT_OF_BAND("Proprietary Out-of-Band"),
    PROPRIETARY_AUTHENTICATION("Proprietary Authentication"),
    PROPRIETARY_GET_CHALLENGE("Proprietary Get Challenge"),
    PROPRIETARY_PREPARATION("Proprietary Preparation");

    private final String value;

}
