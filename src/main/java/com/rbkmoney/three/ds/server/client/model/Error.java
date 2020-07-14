package com.rbkmoney.three.ds.server.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rbkmoney.threeds.server.domain.error.ErrorCode;
import com.rbkmoney.threeds.server.domain.error.ErrorComponent;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
@ToString(onlyExplicitlyIncluded = true)
public class Error {

    private ErrorCode errorCode;
    private ErrorComponent errorComponent;
    private String errorDescription;
    private String errorDetail;
    private String errorMessageType;
    private String sdkTransID;
    private String threeDSServerTransID;
    private String dsTransID;
    private String acsTransID;

}
