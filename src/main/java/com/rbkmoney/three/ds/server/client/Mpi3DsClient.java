package com.rbkmoney.three.ds.server.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.damsel.proxy_provider.PaymentContext;
import com.rbkmoney.damsel.proxy_provider.PaymentResource;
import com.rbkmoney.damsel.proxy_provider.RecurrentTokenContext;
import com.rbkmoney.java.cds.utils.model.CardDataProxyModel;
import com.rbkmoney.three.ds.server.client.configuration.Mpi3DsClientProperties;
import com.rbkmoney.three.ds.server.client.constant.CommonField;
import com.rbkmoney.three.ds.server.client.constant.MessageType;
import com.rbkmoney.three.ds.server.client.constant.MessageVersion;
import com.rbkmoney.three.ds.server.client.constant.Method;
import com.rbkmoney.three.ds.server.client.exception.Mpi3DsClientException;
import com.rbkmoney.three.ds.server.client.model.*;
import com.rbkmoney.three.ds.server.client.utils.generate.GenerateUtils;
import com.rbkmoney.three.ds.server.client.validator.Mpi3DsClientValidator;
import com.rbkmoney.threeds.server.domain.BrowserColorDepth;
import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorAuthenticationInd;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDsMethodCompletionIndicator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static com.rbkmoney.java.damsel.utils.extractors.ProxyProviderPackageExtractors.extractPaymentResource;
import static java.util.Objects.requireNonNull;

@Slf4j
public class Mpi3DsClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final Mpi3DsClientProperties mpiClientProperties;

    public Mpi3DsClient(Mpi3DsClientProperties mpiClientProperties) {
        this(mpiClientProperties, new RestTemplate(), new ObjectMapper());
    }

    public Mpi3DsClient(Mpi3DsClientProperties mpiClientProperties, ObjectMapper objectMapper) {
        this(mpiClientProperties, new RestTemplate(), objectMapper);
    }

    public Mpi3DsClient(Mpi3DsClientProperties mpiClientProperties, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.mpiClientProperties = mpiClientProperties;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public AuthenticationResponse authentication(AuthenticationRequest request) {
        return send(Method.AUTHENTICATION, request, AuthenticationResponse.class);
    }

    public AuthenticationResponse authentication(PaymentContext context, CardDataProxyModel cardData, String notificationUrl) {
        Map<String, String> options = context.getOptions();
        Mpi3DsClientValidator.validate(options);

        String threeDSRequestorID = GenerateUtils.generateTransactionId(context);
        ThreeDSRequestorAuthenticationInd threeDSRequestorAuthenticationInd = prepareThreeDSRequestorAuthenticationInd(context);
        AuthenticationRequest aReq = prepareAuthenticationRequest(threeDSRequestorID, threeDSRequestorAuthenticationInd, options, cardData, notificationUrl);
        return authentication(aReq);
    }

    public AuthenticationResponse authentication(RecurrentTokenContext context, CardDataProxyModel cardData, String notificationUrl) {
        Map<String, String> options = context.getOptions();
        Mpi3DsClientValidator.validate(options);

        String threeDSRequestorID = GenerateUtils.generateTransactionId(context);
        ThreeDSRequestorAuthenticationInd threeDSRequestorAuthenticationInd = prepareThreeDSRequestorAuthenticationInd(context);
        AuthenticationRequest request = prepareAuthenticationRequest(threeDSRequestorID, threeDSRequestorAuthenticationInd, options, cardData, notificationUrl);
        return authentication(request);
    }

    private <T> T send(Method method, AuthenticationRequest request, Class<T> response) {
        log.info("{}. Send request message to DS: request={}", method.getValue(), request);
        try {
            HttpHeaders headers = prepareHttpHeaders();
            String jsonObject = objectMapper.writeValueAsString(request);
            log.info("{}. JSON={}", method.getValue(), jsonObject);
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonObject, headers);

            ResponseEntity<T> responseEntity = restTemplate.exchange(
                    mpiClientProperties.getClient().getUrl(), HttpMethod.POST, requestEntity, response
            );

            requireNonNull(responseEntity);
            log.info("{}. Receive response message from DS: response={}", method.getValue(), response);
            return responseEntity.getBody();
        } catch (Exception ex) {
            // TODO: handle error DS server ?
            throw new Mpi3DsClientException(ex);
        }
    }

    private AuthenticationRequest prepareAuthenticationRequest(
            String threeDSRequestorID,
            ThreeDSRequestorAuthenticationInd threeDSRequestorAuthenticationInd,
            Map<String, String> options,
            CardDataProxyModel cardData,
            String notificationUrl
    ) {
        return AuthenticationRequest.builder()
                .message(MessageData.builder()
                        .version(MessageVersion.VERSION_2_2_0)
                        .type(MessageType.PROPRIETARY_AUTHENTICATION_REQUEST_MESSAGE)
                        .category(MessageCategory.PAYMENT_AUTH)
                        .build())
                .cardholderName(cardData.getCardholderName())
                .device(Device.builder()
                        .channel(DeviceChannel.BROWSER)
                        .build())
                .threeDS(ThreeDS.builder()
                        .compInd(ThreeDsMethodCompletionIndicator.SUCCESSFULLY_COMPLETED)
                        .requestorID(threeDSRequestorID)
                        .requestorName(options.get(CommonField.THREE_DS_REQUESTOR_NAME.getValue()))
                        .requestorURL(options.get(CommonField.THREE_DS_REQUESTOR_URL.getValue()))
                        .requestorAuthenticationInd(threeDSRequestorAuthenticationInd)
                        .build())
                // TODO: expect a protocol change to get the structure Browser
                .browser(Browser.builder()
                        .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                        .javaEnabled(true)
                        .javascriptEnabled(true)
                        .language("ru-RU")
                        .colorDepth(BrowserColorDepth.BITS_32)
                        .screenHeight("1920")
                        .screenWidth("1080")
                        .tz("0")
                        .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0")
                        .build())
                .cardholderAccountIdentifier(cardData.getPan())
                .notificationURL(notificationUrl)
                .build();
    }

    private HttpHeaders prepareHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private ThreeDSRequestorAuthenticationInd prepareThreeDSRequestorAuthenticationInd(PaymentContext context) {
        PaymentResource paymentResource = extractPaymentResource(context);
        if (paymentResource.isSetDisposablePaymentResource()) {
            return ThreeDSRequestorAuthenticationInd.PAYMENT_TRANSACTION;
        }
        return ThreeDSRequestorAuthenticationInd.RECURRING_TRANSACTION;
    }

    private ThreeDSRequestorAuthenticationInd prepareThreeDSRequestorAuthenticationInd(RecurrentTokenContext context) {
        return ThreeDSRequestorAuthenticationInd.PAYMENT_TRANSACTION;
    }

}
