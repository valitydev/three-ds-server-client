package com.rbkmoney.threeds.server.client.utils;

import com.rbkmoney.damsel.domain.BankCardPaymentSystem;
import com.rbkmoney.damsel.domain.PaymentTool;
import com.rbkmoney.damsel.domain.TransactionInfo;
import com.rbkmoney.damsel.proxy_provider.*;
import com.rbkmoney.java.cds.utils.creators.CdsPackageCreators;
import com.rbkmoney.java.cds.utils.model.CardDataProxyModel;
import com.rbkmoney.java.damsel.constant.MpiState;
import com.rbkmoney.java.damsel.utils.creators.DomainPackageCreators;

import java.util.HashMap;
import java.util.Map;

import static com.rbkmoney.java.damsel.utils.creators.DomainPackageCreators.createDisposablePaymentResource;
import static com.rbkmoney.java.damsel.utils.creators.DomainPackageCreators.*;
import static com.rbkmoney.java.damsel.utils.creators.ProxyProviderPackageCreators.createInvoice;
import static com.rbkmoney.java.damsel.utils.creators.ProxyProviderPackageCreators.*;

public class DamselUtils {

    private static final String SESSION_ID = "session_id";
    private static final String DEFAULT_BIN = "123456";
    private static final String CARDHOLDERNAME_NONAME = "NONAME";

    public static CardDataProxyModel getCardDataProxyModel() {
        return CardDataProxyModel.builder()
                .cardholderName("asd")
                .pan("12345")
                .expMonth(Byte.parseByte("01"))
                .expYear(Short.parseShort("20"))
                .build();
    }

    public static PaymentContext getPaymentContext() {
        BankCardModel bankCardModel = createCardData3dsEnrollmentBc();

        PaymentInfo paymentInfo = getPaymentInfo(bankCardModel, null);

        return createContext(
                paymentInfo,
                createSession(
                        createTargetProcessed(),
                        null
                ),
                getProxyOptionsWithOut3ds()
        );
    }

    private static PaymentInfo getPaymentInfo(BankCardModel bankCardModel, TransactionInfo transactionInfo) {
        Invoice invoice = createInvoice(
                "id",
                "2016-06-02",
                prepareCash()
        );


        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setInvoice(invoice);

        InvoicePayment payment = getInvoicePayment(
                bankCardModel,
                getPaymentResource(bankCardModel),
                createPaymentTool(
                        bankCardModel.getBankCard()
                ),
                transactionInfo
        );

        paymentInfo.setPayment(payment);

        InvoicePaymentRefund refund = new InvoicePaymentRefund();
        refund.setCash(prepareCash());

        refund.setId("refund_id");

        TransactionInfo trxInfo = new TransactionInfo();
        trxInfo.setExtra(new HashMap<>());
        trxInfo.setId("trx_id");
        refund.setTrx(transactionInfo);

        paymentInfo.setRefund(refund);

        InvoicePaymentCapture invoicePaymentCapture = new InvoicePaymentCapture();
        invoicePaymentCapture.setCost(prepareCaptureCash());
        paymentInfo.setCapture(invoicePaymentCapture);

        return paymentInfo;
    }

    private static Cash prepareCash() {
        return createCash(
                createCurrency(
                        "Rubles",
                        (short) 643,
                        "RUB",
                        (short) 2
                ),
                10100L
        );
    }

    private static InvoicePayment getInvoicePayment(BankCardModel bankCardModel, PaymentResource paymentResource, PaymentTool paymentTool, TransactionInfo transactionInfo) {
        InvoicePayment payment = new InvoicePayment();
        payment.setId("id");
        payment.setCreatedAt("2016-06-02");
        payment.setPaymentResource(paymentResource);
        payment.setCost(prepareCash());
        payment.setTrx(transactionInfo);
        return payment;
    }

    private static PaymentResource getPaymentResource(BankCardModel bankCardModel) {
        return createPaymentResourceDisposablePaymentResource(
                createDisposablePaymentResource(
                        createClientInfo(
                                "fingerprint",
                                "0.0.0.0"
                        ),
                        bankCardModel.getSessionId(),
                        createPaymentTool(
                                bankCardModel.getBankCard())
                )
        );
    }

    private static Cash prepareCaptureCash() {
        return createCash(
                createCurrency(
                        "Rubles",
                        (short) 643,
                        "RUB",
                        (short) 2
                ),
                10100L
        );
    }

    private static BankCardModel createCardData3dsEnrollmentBc() {
        return createBankCardModel(
                "5100476090795048",
                "06",
                "2019",
                CARDHOLDERNAME_NONAME,
                BankCardPaymentSystem.visa,
                "099",
                null,
                null);
    }

    private static BankCardModel createBankCardModel(
            String pan,
            String month,
            String year,
            String cardholderName,
            BankCardPaymentSystem bankCardPaymentSystem,
            String cvv,
            String cryptogram,
            String eci) {
        com.rbkmoney.cds.storage.CardData cardData = CdsPackageCreators.createCardData(pan);
        return BankCardModel.builder()
                .cardData(cardData)
                .bankCard(
                        DomainPackageCreators.createBankCard(month, year, cardholderName)
                                .setPaymentSystem(bankCardPaymentSystem)
                                .setBin(DEFAULT_BIN)
                                .setLastDigits(cardData.pan.substring(cardData.pan.length() - 4)))
                .cvv(cvv)
                .cryptogram(cryptogram)
                .eci(eci)
                .sessionId(SESSION_ID)
                .build();
    }

    private static Map<String, String> getProxyOptionsWithOut3ds() {
        Map<String, String> options = new HashMap<>();
        options.put(MpiState.MPI_STATE, MpiState.DISABLE);
        return options;
    }
}
