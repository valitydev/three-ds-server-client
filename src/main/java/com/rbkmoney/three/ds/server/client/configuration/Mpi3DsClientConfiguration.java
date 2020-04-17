package com.rbkmoney.three.ds.server.client.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.three.ds.server.client.Mpi3DsClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties({Mpi3DsClientProperties.class})
public class Mpi3DsClientConfiguration {

    @Bean
    public Mpi3DsClient mpi3DsClient(Mpi3DsClientProperties mpi3DsClientProperties) {
        return new Mpi3DsClient(mpi3DsClientProperties, prepareRestTemplate(), prepareObjectMapper());
    }

    private RestTemplate prepareRestTemplate() {
        return new RestTemplate();
    }

    private ObjectMapper prepareObjectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.findAndRegisterModules();
        return om;
    }

}
