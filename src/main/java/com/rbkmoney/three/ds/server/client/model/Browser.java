package com.rbkmoney.three.ds.server.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rbkmoney.threeds.server.domain.BrowserColorDepth;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
@ToString(onlyExplicitlyIncluded = true)
public class Browser {

    @JsonProperty("AcceptHeader")
    private String acceptHeader;

    @JsonProperty("IP")
    private String ip;

    @JsonProperty("JavaEnabled")
    private Boolean javaEnabled;

    @JsonProperty("JavascriptEnabled")
    private Boolean javascriptEnabled;

    @JsonProperty("Language")
    private String language;

    @JsonProperty("ColorDepth")
    private BrowserColorDepth colorDepth;

    @JsonProperty("browserScreenHeight")
    private String screenHeight;

    @JsonProperty("browserScreenWidth")
    private String screenWidth;

    @JsonProperty("TZ")
    private String tz;

    @JsonProperty("UserAgent")
    private String userAgent;

}
