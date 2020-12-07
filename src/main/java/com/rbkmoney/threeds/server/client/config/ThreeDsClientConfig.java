package com.rbkmoney.threeds.server.client.config;

import com.rbkmoney.threeds.server.client.ThreeDsClient;
import com.rbkmoney.threeds.server.client.config.properties.ThreeDsClientProperties;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
@ConditionalOnProperty(name = "client.three-ds-server.enabled", havingValue = "true")
public class ThreeDsClientConfig {

    @Bean
    public ThreeDsClient threeDsClient(RestTemplate threeDsClientRestTemplate, ThreeDsClientProperties threeDsClientProperties) {
        return new ThreeDsClient(threeDsClientRestTemplate, threeDsClientProperties);
    }

    @Bean
    public RestTemplate threeDsClientRestTemplate(ThreeDsClientProperties threeDsClientProperties) {
        return new RestTemplateBuilder()
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(HttpClients.createDefault()))
                .setConnectTimeout(Duration.ofMillis(threeDsClientProperties.getConnectTimeout()))
                .setReadTimeout(Duration.ofMillis(threeDsClientProperties.getReadTimeout()))
                .build();
    }

    @Bean
    @ConfigurationProperties("client.three-ds-server")
    public ThreeDsClientProperties threeDsClientProperties() {
        return new ThreeDsClientProperties();
    }
}
