package com.rbkmoney.three.ds.server.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
@ToString(onlyExplicitlyIncluded = true)
public class CRes {

    @ToString.Include
    private String messageType;

    @ToString.Include
    private String messageVersion;

    // TODO: base64 format, decode. Need list fields
    @ToString.Include
    private String htmlCreq;

}
