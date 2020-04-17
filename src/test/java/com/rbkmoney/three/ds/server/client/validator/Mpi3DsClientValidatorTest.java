package com.rbkmoney.three.ds.server.client.validator;

import com.rbkmoney.three.ds.server.client.constant.CommonField;
import com.rbkmoney.three.ds.server.client.exception.Mpi3DsClientException;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Mpi3DsClientValidatorTest {

    private Mpi3DsClientValidator validator;

    @Before
    public void init() {
        validator = new Mpi3DsClientValidator();
    }

    @Test(expected = Mpi3DsClientException.class)
    public void validatorExceptionTest() {
        validator.validate(Collections.emptyMap());
    }

    @Test
    public void validatorRequiredFieldsTest() {
        Map<String, String> options = new HashMap<>();
        options.put(CommonField.THREE_DS_REQUESTOR_NAME.getValue(), "THREE_DS_REQUESTOR_NAME");
        options.put(CommonField.THREE_DS_REQUESTOR_URL.getValue(), "THREE_DS_REQUESTOR_URL");
        validator.validate(options);
    }

}