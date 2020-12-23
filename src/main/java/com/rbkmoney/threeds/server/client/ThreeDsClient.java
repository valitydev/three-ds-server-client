package com.rbkmoney.threeds.server.client;

import com.rbkmoney.damsel.proxy_provider.PaymentContext;
import com.rbkmoney.damsel.proxy_provider.PaymentResource;
import com.rbkmoney.java.cds.utils.model.CardDataProxyModel;
import com.rbkmoney.threeds.server.client.config.properties.ThreeDsClientProperties;
import com.rbkmoney.threeds.server.domain.Valuable;
import com.rbkmoney.threeds.server.domain.account.*;
import com.rbkmoney.threeds.server.domain.address.AddressMatch;
import com.rbkmoney.threeds.server.domain.browser.BrowserColorDepth;
import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.domain.merchant.DeliveryTimeframe;
import com.rbkmoney.threeds.server.domain.merchant.MerchantRiskIndicatorWrapper;
import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.message.MessageExtension;
import com.rbkmoney.threeds.server.domain.order.PreOrderPurchaseInd;
import com.rbkmoney.threeds.server.domain.order.ReorderItemsInd;
import com.rbkmoney.threeds.server.domain.phone.Phone;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyAuthenticationRequest;
import com.rbkmoney.threeds.server.domain.ship.ShipAddressUsageInd;
import com.rbkmoney.threeds.server.domain.ship.ShipIndicator;
import com.rbkmoney.threeds.server.domain.ship.ShipNameIndicator;
import com.rbkmoney.threeds.server.domain.threedsmethod.ThreeDsMethodData;
import com.rbkmoney.threeds.server.domain.threedsmethod.ThreeDsMethodRequest;
import com.rbkmoney.threeds.server.domain.threedsmethod.ThreeDsMethodResponse;
import com.rbkmoney.threeds.server.domain.threedsrequestor.*;
import com.rbkmoney.threeds.server.domain.transaction.TransactionType;
import com.rbkmoney.threeds.server.domain.unwrapped.Address;
import com.rbkmoney.threeds.server.domain.versioning.ThreeDsVersionRequest;
import com.rbkmoney.threeds.server.domain.versioning.ThreeDsVersionResponse;
import com.rbkmoney.threeds.server.domain.whitelist.WhiteListStatus;
import com.rbkmoney.threeds.server.domain.whitelist.WhiteListStatusSource;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;
import com.rbkmoney.threeds.server.serialization.ListWrapper;
import com.rbkmoney.threeds.server.serialization.TemporalAccessorWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.rbkmoney.java.damsel.utils.extractors.ProxyProviderPackageExtractors.extractPaymentResource;
import static com.rbkmoney.threeds.server.utils.AccountNumberUtils.hideAccountNumber;

@RequiredArgsConstructor
@Slf4j
public class ThreeDsClient {

    private static final String REQUEST_LOG = "-> Req [{}]: {}";
    private static final String RESPONSE_LOG = "<- Res [{}]: {}";

    private final RestTemplate restTemplate;
    private final ThreeDsClientProperties properties;

    public Optional<ThreeDsVersionResponse> threeDsVersioning(String accountNumber) {
        String url = properties.getVersioningUrl();
        String endpoint = "POST " + url;

        log.info(REQUEST_LOG, endpoint, hideAccountNumber(accountNumber));

        ThreeDsVersionRequest request = threeDsVersionRequest(accountNumber);

        Optional<ThreeDsVersionResponse> response = Optional.ofNullable(
                restTemplate.postForObject(url, request, ThreeDsVersionResponse.class));

        log.info(RESPONSE_LOG, endpoint, response.toString());

        return response;
    }

    public Optional<ThreeDsMethodResponse> threeDsMethod(
            String threeDsServerTransId,
            String threeDsMethodNotificationUrl,
            String threeDsMethodUrl) {
        String url = properties.getThreeDsMethodUrl();
        String endpoint = "POST " + url;

        ThreeDsMethodRequest request = threeDsMethodRequest(threeDsServerTransId, threeDsMethodNotificationUrl, threeDsMethodUrl);

        log.info(REQUEST_LOG, endpoint, request.toString());

        Optional<ThreeDsMethodResponse> response = Optional.ofNullable(
                restTemplate.postForObject(url, request, ThreeDsMethodResponse.class));

        log.info(RESPONSE_LOG, endpoint, response.toString());

        return response;
    }

    public Optional<Message> threeDsAuthentication(
            ThreeDsMethodCompletionInd threeDsMethodCompletionInd,
            PaymentContext context,
            String threeDsServerTransId,
            CardDataProxyModel cardDataProxyModel,
            String dsEndProtocolVersion) {
        RBKMoneyAuthenticationRequest rbkMoneyAuthenticationRequest = rbkMoneyAuthenticationRequest(
                threeDsMethodCompletionInd, context, threeDsServerTransId, cardDataProxyModel, dsEndProtocolVersion);

        return threeDsAuthentication(rbkMoneyAuthenticationRequest);
    }

    public Optional<Message> threeDsAuthentication(RBKMoneyAuthenticationRequest rbkMoneyAuthenticationRequest) {
        String url = properties.getSdkUrl();
        String endpoint = "POST " + url;

        log.info(REQUEST_LOG, endpoint, rbkMoneyAuthenticationRequest.toString());

        Optional<Message> response = Optional.ofNullable(
                restTemplate.postForObject(url, rbkMoneyAuthenticationRequest, Message.class));

        log.info(RESPONSE_LOG, endpoint, response.toString());

        return response;
    }

    private RBKMoneyAuthenticationRequest rbkMoneyAuthenticationRequest(
            ThreeDsMethodCompletionInd threeDsMethodCompletionInd,
            PaymentContext context,
            String threeDsServerTransId,
            CardDataProxyModel cardDataProxyModel,
            String dsEndProtocolVersion) {
        RBKMoneyAuthenticationRequest request = RBKMoneyAuthenticationRequest.builder()
                .threeDSCompInd(wrapEnum(threeDsMethodCompletionInd))
                .threeDSRequestorAuthenticationInd(wrapEnum(prepareThreeDSRequestorAuthenticationInd(context))) //todo
                .threeDSRequestorAuthenticationInfo(getThreeDSRequestorAuthenticationInfo()) //todo
                .threeDSRequestorChallengeInd(wrapEnum(ThreeDSRequestorChallengeInd.CHALLENGE_REQUESTED_MANDATE)) //todo
                .threeDSRequestorDecMaxTime("00015") //todo mb null
                .threeDSRequestorDecReqInd(wrapEnum(ThreeDSRequestorDecReqInd.DO_NOT_USE_DECOUPLED_AUTH)) //todo mb null
                .threeDSRequestorPriorAuthenticationInfo(getThreeDSRequestorPriorAuthenticationInfo()) //todo mb null
                .threeDSServerTransID(threeDsServerTransId)
                .threeRIInd(wrapEnum(ThreeRIInd.ACCOUNT_VERIFICATION)) //todo
                .acctType(wrapEnum(AccountType.DEBIT)) //todo
                .acquirerBIN("400551") //todo
                .acquirerMerchantID("3586248519") //todo
                .addrMatch(wrapEnum(AddressMatch.SAME_ADDRESS)) //todo
                .broadInfo(null)
                .browserAcceptHeader("qinmrrqtwt") //todo
                .browserIP("192.168.0.1") //todo
                .browserJavaEnabled(true) //todo
                .browserJavascriptEnabled(false) //todo
                .browserLanguage("njuvdt") //todo
                .browserColorDepth(wrapEnum(BrowserColorDepth.BITS_4)) //todo
                .browserScreenHeight("1289") //todo
                .browserScreenWidth("7372") //todo
                .browserTZ("3138") //todo
                .browserUserAgent("ilcytizmjr") //todo
                .cardExpiryDate(getCardExpiryDate(cardDataProxyModel)) //todo YYMM
                .acctInfo(getAcctInfo()) //todo
                .acctNumber(cardDataProxyModel.getPan())
                .acctID("kkdfnzruts") //todo
                .billingAddress(getBillingAddress()) //todo
                .email("support@rbk.money") //todo
                .homePhone(getHomePhone()) //todo
                .mobilePhone(getHomePhone()) //todo
                .cardholderName(cardDataProxyModel.getCardholderName())
                .shippingAddress(getBillingAddress()) //todo
                .workPhone(getHomePhone()) //todo
                .deviceChannel(wrapEnum(DeviceChannel.BROWSER)) //todo
                .payTokenInd(null) //todo mb null
                .payTokenSource(null) //todo mb null
                .purchaseInstalData("66") //todo
                .mcc("6852") //todo
                .merchantCountryCode("992") //todo
                .merchantName("ozcjccddpo") //todo
                .merchantRiskIndicator(getMerchantRiskIndicator()) //todo
                .messageCategory(wrapEnum(MessageCategory.PAYMENT_AUTH)) //todo
                .messageExtension(wrapList(getMessageExtension())) //todo mb null
                .notificationURL("https://rbk.money/") //todo
                .purchaseAmount("44823") //todo
                .purchaseCurrency("643") //todo
                .purchaseExponent("purchaseExponent") //todo
                .purchaseDate(wrapTime(LocalDateTime.now())) //todo
                .recurringExpiry(wrapTime(LocalDate.now())) //todo
                .recurringFrequency("776") //todo
                .transType(wrapEnum(TransactionType.CHECK_ACCEPTANCE)) //todo
                .whiteListStatus(wrapEnum(WhiteListStatus.WHITELISTED)) //todo mb null
                .whiteListStatusSource(wrapEnum(WhiteListStatusSource.THREE_DS_SERVER)) //todo mb null
                .build();
        request.setMessageVersion(dsEndProtocolVersion);
        return request;
    }

    private ThreeDSRequestorAuthenticationInd prepareThreeDSRequestorAuthenticationInd(PaymentContext context) {
        PaymentResource paymentResource = extractPaymentResource(context);
        if (paymentResource.isSetDisposablePaymentResource()) {
            return ThreeDSRequestorAuthenticationInd.PAYMENT_TRANSACTION;
        }

        return ThreeDSRequestorAuthenticationInd.RECURRING_TRANSACTION;
    }

    private ThreeDSRequestorAuthenticationInfoWrapper getThreeDSRequestorAuthenticationInfo() {
        ThreeDSRequestorAuthenticationInfoWrapper wrapper = new ThreeDSRequestorAuthenticationInfoWrapper();
        wrapper.setThreeDSReqAuthData("rtkrcxbcdg"); //todo
        wrapper.setThreeDSReqAuthMethod(wrapEnum(ThreeDSReqAuthMethod.FEDERATED_ID)); //todo
        wrapper.setThreeDSReqAuthTimestamp(wrapTime(LocalDateTime.now())); //todo
        return wrapper;
    }

    private ThreeDSRequestorPriorAuthenticationInfoWrapper getThreeDSRequestorPriorAuthenticationInfo() {
        ThreeDSRequestorPriorAuthenticationInfoWrapper wrapper = new ThreeDSRequestorPriorAuthenticationInfoWrapper();
        wrapper.setThreeDSReqPriorRef("eeb3ff4a-a248-48d8-b4cd-e52046bc7fbc"); //todo
        wrapper.setThreeDSReqPriorAuthTimestamp(wrapTime(LocalDateTime.now())); //todo
        wrapper.setThreeDSReqPriorAuthMethod(wrapEnum(ThreeDSReqPriorAuthMethod.OTHER_METHODS)); //todo
        wrapper.setThreeDSReqPriorAuthData("qoeargloeu"); //todo
        return wrapper;
    }

    private String getCardExpiryDate(CardDataProxyModel cardDataProxyModel) {
        String year = String.valueOf(cardDataProxyModel.getExpYear());
        int length = year.length();
        if (length > 2) {
            year = year.substring(length - 2);
        }
        String month = String.valueOf(cardDataProxyModel.getExpMonth());
        return year + month;
    }

    private AccountInfoWrapper getAcctInfo() {
        AccountInfoWrapper wrapper = new AccountInfoWrapper();
        wrapper.setChAccDate(wrapTime(LocalDate.now())); //todo
        wrapper.setShipAddressUsageInd(wrapEnum(ShipAddressUsageInd.FROM_30_TO_60_DAYS)); //todo
        wrapper.setTxnActivityYear("1"); //todo
        wrapper.setPaymentAccAge(wrapTime(LocalDate.now())); //todo
        wrapper.setPaymentAccInd(wrapEnum(PaymentAccInd.FROM_30_TO_60_DAYS)); //todo
        wrapper.setShipNameIndicator(wrapEnum(ShipNameIndicator.ACCOUNT_NAME_DIFFERENT)); //todo
        wrapper.setChAccPwChangeInd(wrapEnum(ChAccPwChangeInd.FROM_30_TO_60_DAYS)); //todo
        wrapper.setProvisionAttemptsDay("395"); //todo
        wrapper.setChAccChange(wrapTime(LocalDate.now())); //todo
        wrapper.setChAccChangeInd(wrapEnum(ChAccChangeInd.FROM_30_TO_60_DAYS)); //todo
        wrapper.setNbPurchaseAccount("199"); //todo
        wrapper.setChAccAgeInd(wrapEnum(ChAccAgeInd.FROM_30_TO_60_DAYS)); //todo
        wrapper.setChAccPwChange(wrapTime(LocalDate.now())); //todo
        wrapper.setShipAddressUsage(wrapTime(LocalDate.now())); //todo
        wrapper.setTxnActivityDay("1"); //todo
        wrapper.setSuspiciousAccActivity(wrapEnum(SuspiciousAccActivity.SUSPICIOUS_ACTIVITY_OBSERVED)); //todo
        return wrapper;
    }

    private Address getBillingAddress() {
        Address address = new Address();
        address.setAddrCity("tjkaonpcnw"); //todo
        address.setAddrCountry("958"); //todo
        address.setAddrLine1("dogwkabiut"); //todo
        address.setAddrLine2("dmdznwphbq"); //todo
        address.setAddrLine3("fsrxwbavtz"); //todo
        address.setAddrPostCode("48651"); //todo
        address.setAddrState("839"); //todo
        return address;
    }

    private Phone getHomePhone() {
        Phone phone = new Phone();
        phone.setCc("35"); //todo
        phone.setSubscriber("59"); //todo
        return phone;
    }

    private MerchantRiskIndicatorWrapper getMerchantRiskIndicator() {
        MerchantRiskIndicatorWrapper wrapper = new MerchantRiskIndicatorWrapper();
        wrapper.setDeliveryTimeframe(wrapEnum(DeliveryTimeframe.ELECTRONIC_DELIVERY)); //todo
        wrapper.setDeliveryEmailAddress("support@rbk.money"); //todo
        wrapper.setGiftCardCurr("744"); //todo
        wrapper.setPreOrderDate(wrapTime(LocalDate.now())); //todo
        wrapper.setShipIndicator(wrapEnum(ShipIndicator.ANOTHER_VERIFIED_ADDRESS)); //todo
        wrapper.setGiftCardAmount("7659926414"); //todo
        wrapper.setPreOrderPurchaseInd(wrapEnum(PreOrderPurchaseInd.FUTURE_AVAILABILITY)); //todo
        wrapper.setGiftCardCount("53"); //todo
        wrapper.setReorderItemsInd(wrapEnum(ReorderItemsInd.FIRST_TIME_ORDERED)); //todo
        return wrapper;
    }

    private MessageExtension getMessageExtension() {
        MessageExtension messageExtension = new MessageExtension();
        messageExtension.setData(Collections.singletonMap("ysnpvgpipa", "nssrskxjge")); //todo
        messageExtension.setName("yudlqxonex"); //todo
        messageExtension.setCriticalityIndicator(false); //todo
        messageExtension.setId("hkdohdcymz"); //todo
        return messageExtension;
    }

    private ThreeDsVersionRequest threeDsVersionRequest(String accountNumber) {
        return ThreeDsVersionRequest.builder()
                .accountNumber(accountNumber)
                .build();
    }

    private ThreeDsMethodRequest threeDsMethodRequest(String threeDsServerTransId, String threeDsMethodNotificationUrl, String threeDsMethodUrl) {
        return ThreeDsMethodRequest.builder()
                .threeDsMethodData(
                        ThreeDsMethodData.builder()
                                .threeDSServerTransID(threeDsServerTransId)
                                .threeDSMethodNotificationURL(threeDsMethodNotificationUrl)
                                .build())
                .threeDsMethodUrl(threeDsMethodUrl)
                .build();
    }

    private <T extends Valuable> EnumWrapper<T> wrapEnum(T value) {
        EnumWrapper<T> wrapper = new EnumWrapper<>();
        wrapper.setValue(value);
        return wrapper;
    }

    private <T extends TemporalAccessor> TemporalAccessorWrapper<T> wrapTime(T value) {
        TemporalAccessorWrapper<T> wrapper = new TemporalAccessorWrapper<>();
        wrapper.setValue(value);
        return wrapper;
    }

    private <T> ListWrapper<T> wrapList(T... values) {
        ListWrapper<T> wrapper = new ListWrapper<>();
        wrapper.setValue(Arrays.asList(values));
        return wrapper;
    }

    private <T> ListWrapper<T> wrapList(List<T> value) {
        ListWrapper<T> wrapper = new ListWrapper<>();
        wrapper.setValue(value);
        return wrapper;
    }
}
