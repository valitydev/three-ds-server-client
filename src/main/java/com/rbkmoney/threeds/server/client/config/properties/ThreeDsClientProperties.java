package com.rbkmoney.threeds.server.client.config.properties;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ThreeDsClientProperties {

    private boolean enabled;
    @Length(max = 2048)
    private String sdkUrl;
    @Length(max = 2048)
    private String versioningUrl;
    @Length(max = 2048)
    private String threeDsMethodUrl;
    private int readTimeout;
    private int connectTimeout;

}
