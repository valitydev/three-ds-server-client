package com.rbkmoney.three.ds.server.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.damsel.proxy_provider.PaymentContext;
import com.rbkmoney.damsel.proxy_provider.PaymentResource;
import com.rbkmoney.damsel.proxy_provider.RecurrentTokenContext;
import com.rbkmoney.java.cds.utils.model.CardDataProxyModel;
import com.rbkmoney.three.ds.server.client.configuration.Mpi3DsClientProperties;
import com.rbkmoney.three.ds.server.client.constant.CommonField;
import com.rbkmoney.three.ds.server.client.constant.Method;
import com.rbkmoney.three.ds.server.client.exception.Mpi3DsClientException;
import com.rbkmoney.three.ds.server.client.utils.generate.GenerateUtils;
import com.rbkmoney.three.ds.server.client.validator.Mpi3DsClientValidator;
import com.rbkmoney.threeds.server.domain.BrowserColorDepth;
import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.AReq;
import com.rbkmoney.threeds.server.domain.root.emvco.ARes;
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

    public ARes authentication(AReq areq) {
        return send(Method.AUTHENTICATION, areq, ARes.class);
    }

    public ARes authentication(PaymentContext context, CardDataProxyModel cardData, String notificationUrl) {
        Map<String, String> options = context.getOptions();
        Mpi3DsClientValidator validator = new Mpi3DsClientValidator();
        validator.validate(options);

        String threeDSRequestorID = GenerateUtils.generateTransactionId(context);
        ThreeDSRequestorAuthenticationInd threeDSRequestorAuthenticationInd = prepareThreeDSRequestorAuthenticationInd(context);
        AReq aReq = prepareAReq(threeDSRequestorID, threeDSRequestorAuthenticationInd, options, cardData, notificationUrl);
        return authentication(aReq);
    }

    public ARes authentication(RecurrentTokenContext context, CardDataProxyModel cardData, String notificationUrl) {
        Map<String, String> options = context.getOptions();
        Mpi3DsClientValidator validator = new Mpi3DsClientValidator();
        validator.validate(options);

        String threeDSRequestorID = GenerateUtils.generateTransactionId(context);
        ThreeDSRequestorAuthenticationInd threeDSRequestorAuthenticationInd = prepareThreeDSRequestorAuthenticationInd(context);
        AReq aReq = prepareAReq(threeDSRequestorID, threeDSRequestorAuthenticationInd, options, cardData, notificationUrl);
        return authentication(aReq);
    }

    private <T> T send(Method method, Message request, Class<T> response) {
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

    private AReq prepareAReq(String threeDSRequestorID, ThreeDSRequestorAuthenticationInd threeDSRequestorAuthenticationInd, Map<String, String> options, CardDataProxyModel cardData, String notificationUrl) {
        AReq aReq = new AReq();
        // TODO: expect a protocol change to get the structure Browser
        aReq.setMessageVersion("2.1.0");
        aReq.setMessageCategory(MessageCategory.PAYMENT_AUTH);
        aReq.setCardholderName(cardData.getCardholderName());
        aReq.setDeviceChannel(DeviceChannel.BROWSER);

        aReq.setThreeDSCompInd(ThreeDsMethodCompletionIndicator.SUCCESSFULLY_COMPLETED);
        aReq.setThreeDSRequestorID(threeDSRequestorID);

        aReq.setThreeDSRequestorName(options.get(CommonField.THREE_DS_REQUESTOR_NAME.getValue()));
        aReq.setThreeDSRequestorURL(options.get(CommonField.THREE_DS_REQUESTOR_URL.getValue()));

        aReq.setBrowserAcceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        aReq.setBrowserJavaEnabled(true);
        aReq.setBrowserJavascriptEnabled(true);
        aReq.setBrowserLanguage("ru-RU");
        aReq.setBrowserColorDepth(BrowserColorDepth.BITS_32);
        aReq.setBrowserScreenHeight("1920");
        aReq.setBrowserScreenWidth("1080");
        aReq.setBrowserTZ("0");
        aReq.setBrowserUserAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:47.0) Gecko/20100101 Firefox/47.0");

        aReq.setAcctID(cardData.getPan());
        aReq.setNotificationURL(notificationUrl);
        aReq.setThreeDSRequestorAuthenticationInd(threeDSRequestorAuthenticationInd);
        return aReq;
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
