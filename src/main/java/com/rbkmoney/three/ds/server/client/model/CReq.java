package com.rbkmoney.three.ds.server.client.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rbkmoney.three.ds.server.client.constant.CommonField;
import com.rbkmoney.threeds.server.domain.ChallengeWindowSize;
import com.rbkmoney.threeds.server.domain.message.MessageType;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
@ToString(onlyExplicitlyIncluded = true)
public class CReq {

    @ToString.Include
    private String threeDSServerTransID;

    @ToString.Include
    private String acsTransID;

    @ToString.Include
    private String messageType = MessageType.CREQ.getValue();

    @ToString.Include
    private String messageVersion;

    // Optional
    @ToString.Include
    private String threeDSSessionData;

    @ToString.Include
    private String challengeWindowSize;

    public Map<String, String> prepareParams() {
        Map<String, String> params = new HashMap<>();
        params.put(CommonField.THREE_DS_SERVER_TRANS_ID.getValue(), threeDSServerTransID);
        params.put(CommonField.ACS_TRANS_ID.getValue(), acsTransID);
        params.put(CommonField.MESSAGE_TYPE.getValue(), messageType);
        params.put(CommonField.MESSAGE_VERSION.getValue(), messageVersion);

        if (threeDSSessionData != null) {
            params.put(CommonField.THREE_DS_SESSION_DATA.getValue(), threeDSSessionData);
        }

        if (challengeWindowSize == null) {
            challengeWindowSize = ChallengeWindowSize.FULL_SCREEN.getValue();
        }

        params.put(CommonField.CHALLENGE_WINDOW_SIZE.getValue(), challengeWindowSize);
        return params;
    }

}
