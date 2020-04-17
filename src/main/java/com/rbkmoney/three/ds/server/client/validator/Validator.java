package com.rbkmoney.three.ds.server.client.validator;

import java.util.Map;

public interface Validator {
    void validate(Map<String, String> options);
}
