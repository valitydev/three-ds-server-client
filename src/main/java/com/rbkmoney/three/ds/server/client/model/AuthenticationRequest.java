package com.rbkmoney.three.ds.server.client.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.rbkmoney.threeds.server.domain.AddressMatch;
import com.rbkmoney.threeds.server.domain.account.AccountInfo;
import com.rbkmoney.threeds.server.domain.account.AccountType;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeRIInd;
import com.rbkmoney.threeds.server.domain.transaction.TransactionType;
import com.rbkmoney.threeds.server.domain.unwrapped.Address;
import com.rbkmoney.threeds.server.domain.whitelist.WhiteListStatus;
import com.rbkmoney.threeds.server.domain.whitelist.WhiteListStatusSource;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
@ToString(onlyExplicitlyIncluded = true)
public class AuthenticationRequest {

    @JsonUnwrapped(prefix = "message")
    private MessageData message;

    @JsonUnwrapped(prefix = "threeDS")
    private ThreeDS threeDS;

    private ThreeRIInd threeRIInd;

    @JsonProperty("acctType")
    private AccountType accountType;

    private String acquirerBIN;
    private String acquirerMerchantID;

    @JsonProperty("addrMatch")
    private AddressMatch addressMatch;

    @JsonProperty("broadInfo")
    private Object broadcastInformation;

    @JsonUnwrapped(prefix = "browser")
    private Browser browser;

    @JsonProperty("acctID")
    private String cardholderAccountIdentifier;

    private String cardholderName;
    private String cardExpiryDate;

    @JsonProperty("acctInfo")
    private AccountInfo cardholderAccountInformation;

    @JsonProperty("acctNumber")
    private String cardholderAccountNumber;

    @JsonUnwrapped(prefix = "bill")
    private Address billingAddress;

    @JsonUnwrapped(prefix = "ship")
    private Address shippingAddress;

    @JsonUnwrapped
    private Contact contact;

    @JsonUnwrapped(prefix = "device")
    private Device device;

    private String dsReferenceNumber;
    private String dsTransID;
    private String dsURL;

    @JsonUnwrapped(prefix = "payToken")
    private PayToken payToken;

    @JsonUnwrapped(prefix = "merchant")
    private Merchant merchant;

    @JsonProperty("mcc")
    private String merchantCategoryCode;

    private String notificationURL;

    @JsonUnwrapped(prefix = "purchase")
    private Purchase purchase;

    @JsonUnwrapped(prefix = "recurring")
    private Recurring recurring;

    @JsonProperty("transType")
    private TransactionType transactionType;

    private String acsURL;

    private WhiteListStatus whiteListStatus;
    private WhiteListStatusSource whiteListStatusSource;

    @JsonIgnore
    private LocalDateTime decoupledAuthMaxTime;

}
