package com.rbkmoney.three.ds.server.client.validator;

import com.rbkmoney.three.ds.server.client.constant.CommonField;
import com.rbkmoney.three.ds.server.client.exception.Mpi3DsClientException;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Map;

@NoArgsConstructor
public class Mpi3DsClientValidator {

    private static String[] requiredFields = {
            CommonField.THREE_DS_REQUESTOR_NAME.getValue(),
            CommonField.THREE_DS_REQUESTOR_URL.getValue()
    };

    public static void validate(Map<String, String> options) {
        boolean result = Arrays.stream(requiredFields).allMatch(options::containsKey);
        if (!result) {
            throw new Mpi3DsClientException(String.format("Not found some required fields %s", Arrays.asList(requiredFields)));
        }
    }

}
