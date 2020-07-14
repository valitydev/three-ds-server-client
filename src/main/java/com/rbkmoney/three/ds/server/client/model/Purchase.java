package com.rbkmoney.three.ds.server.client.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
@ToString(onlyExplicitlyIncluded = true)
public class Purchase {

    @JsonProperty("InstalData")
    private String instalData;

    @JsonProperty("Amount")
    private String amount;

    @JsonProperty("Currency")
    private String currency;

    @JsonProperty("Exponent")
    private String exponent;

    @JsonProperty("Date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMddHHmmss")
    private LocalDateTime date;

}
