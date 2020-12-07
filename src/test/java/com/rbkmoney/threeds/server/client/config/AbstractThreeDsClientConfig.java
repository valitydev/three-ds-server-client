package com.rbkmoney.threeds.server.client.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.rbkmoney.threeds.server.client.utils.JsonMapper;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                AbstractThreeDsClientConfig.TestConfig.class,
                ServletWebServerFactoryAutoConfiguration.class,
                DispatcherServletAutoConfiguration.class,
                WebMvcAutoConfiguration.class},
        properties = "spring.main.allow-bean-definition-overriding=true")
@ContextConfiguration(initializers = AbstractThreeDsClientConfig.Initializer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public abstract class AbstractThreeDsClientConfig {

    @RegisterExtension
    public static ServerExtension serverExtension = new ServerExtension();

    @TestConfiguration
    @Import(ThreeDsClientConfig.class)
    public static class TestConfig {

        @Bean
        public JsonMapper jsonMapper(ObjectMapper objectMapper, ResourceLoader resourceLoader) {
            return new JsonMapper(objectMapper, resourceLoader);
        }

        @Bean
        public RestTemplate restTemplate() {
            return new RestTemplate();
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

    public static class Initializer extends ConfigFileApplicationContextInitializer {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            super.initialize(configurableApplicationContext);
            TestPropertyValues.of(
                    "client.three-ds-server.enabled=true",
                    "client.three-ds-server.sdk-url=http://localhost:" + serverExtension.getServer().port() + "/sdk",
                    "client.three-ds-server.versioning-url=http://localhost:" + serverExtension.getServer().port() + "/versioning",
                    "client.three-ds-server.three-ds-method-url=http://localhost:" + serverExtension.getServer().port() + "/three-ds-method",
                    "client.three-ds-server.readTimeout=10000",
                    "client.three-ds-server.connectTimeout=5000"
            ).applyTo(configurableApplicationContext);
        }
    }

    public static class ServerExtension implements BeforeAllCallback, AfterAllCallback {

        private final WireMockServer server;

        public ServerExtension() {
            this.server = new WireMockServer(wireMockConfig().dynamicPort());
        }


        public WireMockServer getServer() {
            return server;
        }

        @Override
        public void beforeAll(ExtensionContext context) {
            server.start();
            WireMock.configureFor("localhost", server.port());
        }

        @Override
        public void afterAll(ExtensionContext context) {
            server.stop();
            server.resetAll();
        }
    }
}
