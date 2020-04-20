package com.rbkmoney.three.ds.server.client.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * TODO: выпилить при переходе на бой
 */
@Getter
@RequiredArgsConstructor
public enum HeaderField {

    ID("x-ul-testcaserun-id");

    private final String value;
}
