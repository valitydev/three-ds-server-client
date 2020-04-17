package com.rbkmoney.three.ds.server.client.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "mpi")
public class Mpi3DsClientProperties {

    @Valid
    private Client client = new Client();

    @Valid
    private RestTemplateSettings restTemplateSettings = new RestTemplateSettings();

    @Getter
    @Setter
    public static class Client {

        @NotEmpty
        private String url;

    }

    @Getter
    @Setter
    public static class RestTemplateSettings {

        @NotNull
        private int maxTotalPooling;

        @NotNull
        private int defaultMaxPerRoute;

        @NotNull
        private int requestTimeout;

        @NotNull
        private int poolTimeout;

        @NotNull
        private int connectionTimeout;

        @NotNull
        private int validationAfterInactivityMs;

    }

}
