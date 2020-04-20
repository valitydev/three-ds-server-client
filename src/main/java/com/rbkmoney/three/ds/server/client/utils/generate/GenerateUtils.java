package com.rbkmoney.three.ds.server.client.utils.generate;

import com.rbkmoney.damsel.proxy_provider.PaymentContext;
import com.rbkmoney.damsel.proxy_provider.PaymentInfo;
import com.rbkmoney.damsel.proxy_provider.RecurrentTokenContext;
import com.rbkmoney.java.damsel.utils.extractors.ProxyProviderPackageExtractors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenerateUtils {

    public static final String DELIMETER_POINT = ".";

    public static String generateTransactionId(PaymentContext context) {
        return generateTransactionId(context, DELIMETER_POINT);
    }

    public static String generateTransactionId(PaymentContext context, String separator) {
        return generateTransactionId(context.getPaymentInfo(), separator);
    }

    public static String generateTransactionId(PaymentInfo paymentInfo, String separator) {
        return ProxyProviderPackageExtractors.extractInvoiceId(paymentInfo) + separator + ProxyProviderPackageExtractors.extractPaymentId(paymentInfo);
    }

    public static String generateTransactionId(PaymentInfo paymentInfo) {
        return generateTransactionId(paymentInfo, DELIMETER_POINT);
    }

    public static String generateTransactionId(RecurrentTokenContext context) {
        return ProxyProviderPackageExtractors.extractRecurrentId(context);
    }

}
