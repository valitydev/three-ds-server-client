package com.rbkmoney.three.ds.server.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rbkmoney.threeds.server.domain.MerchantRiskIndicator;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
@ToString(onlyExplicitlyIncluded = true)
public class Merchant {

    @JsonProperty("CountryCode")
    private String countryCode;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("RiskIndicator")
    private MerchantRiskIndicator riskIndicator;

}
