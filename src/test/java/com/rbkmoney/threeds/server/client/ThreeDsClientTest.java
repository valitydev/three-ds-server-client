package com.rbkmoney.threeds.server.client;

import com.rbkmoney.threeds.server.client.config.AbstractThreeDsClientConfig;
import com.rbkmoney.threeds.server.client.utils.JsonMapper;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.ErroWrapper;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyAuthenticationResponse;
import com.rbkmoney.threeds.server.domain.threedsmethod.ThreeDsMethodResponse;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDsMethodCompletionInd;
import com.rbkmoney.threeds.server.domain.versioning.ThreeDsVersion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.rbkmoney.threeds.server.client.utils.DamselUtils.getCardDataProxyModel;
import static com.rbkmoney.threeds.server.client.utils.DamselUtils.getPaymentContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ThreeDsClientTest extends AbstractThreeDsClientConfig {

    @Autowired
    private ThreeDsClient threeDsClient;

    @Autowired
    private JsonMapper jsonMapper;

    @Test
    public void shouldReturnThreeDsVersionIfVersioningIsValid() {
        stubFor(get(urlEqualTo("/versioning?account_number=1234567890"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(resource("versioning.json"))));

        Optional<ThreeDsVersion> threeDsVersion = threeDsClient.versioning("1234567890");

        assertTrue(threeDsVersion.isPresent());
        assertEquals("visa", threeDsVersion.get().getDsProviderId());
    }

    @Test
    public void shouldReturnThreeDsMethodResponse() {
        stubFor(post(urlEqualTo("/three-ds-method"))
                .withRequestBody(equalToJson(resource("three-ds-method-request.json"), true, true))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(resource("three-ds-method-response.json"))));

        Optional<ThreeDsMethodResponse> threeDsMethodResponse = threeDsClient.threeDsMethod("1", "url1", "url2");

        assertTrue(threeDsMethodResponse.isPresent());
        assertEquals("1", threeDsMethodResponse.get().getThreeDsServerTransId());
    }

    @Test
    public void shouldReturnRBKMoneyAuthenticationResponseMesage() {
        stubFor(post(urlEqualTo("/sdk"))
                // rbkmoney-authentication-request содержит только часть обязательных данных
                .withRequestBody(equalToJson(resource("rbkmoney-authentication-request.json"), true, true))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        // rbkmoney-authentication-response содержит только часть обязательных данных
                        .withBody(resource("rbkmoney-authentication-response.json"))));

        Optional<Message> message = threeDsClient.emvcoAuthentication(
                ThreeDsMethodCompletionInd.SUCCESSFULLY_COMPLETED,
                getPaymentContext(),
                "threeDsServerTransId",
                getCardDataProxyModel(),
                "2.1.0"
        );

        assertTrue(message.isPresent());
        assertTrue(message.get() instanceof RBKMoneyAuthenticationResponse);
        assertEquals("acsURL", ((RBKMoneyAuthenticationResponse) message.get()).getAcsURL());
    }

    @Test
    public void shouldReturnErrorMessage() {
        stubFor(post(urlEqualTo("/sdk"))
                // rbkmoney-authentication-request содержит только часть обязательных данных
                .withRequestBody(equalToJson(resource("rbkmoney-authentication-request.json"), true, true))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(resource("error.json"))));

        Optional<Message> message = threeDsClient.emvcoAuthentication(
                ThreeDsMethodCompletionInd.SUCCESSFULLY_COMPLETED,
                getPaymentContext(),
                "threeDsServerTransId",
                getCardDataProxyModel(),
                "2.1.0"
        );

        assertTrue(message.isPresent());
        assertTrue(message.get() instanceof ErroWrapper);
        assertEquals("2.1.0", message.get().getMessageVersion());
    }

    private String resource(String fullPath) {
        return jsonMapper.readStringFromFile(fullPath);
    }
}
