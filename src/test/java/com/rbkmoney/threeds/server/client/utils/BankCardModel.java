package com.rbkmoney.threeds.server.client.utils;

import com.rbkmoney.cds.storage.CardData;
import com.rbkmoney.damsel.domain.BankCard;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankCardModel {

    private CardData cardData;
    private BankCard bankCard;
    private String cvv;
    private String sessionId;
    private String cryptogram;
    private String eci;

}
