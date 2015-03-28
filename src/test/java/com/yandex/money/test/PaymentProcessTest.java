package com.yandex.money.test;

import com.yandex.money.api.methods.ProcessExternalPayment;
import com.yandex.money.api.methods.ProcessPayment;
import com.yandex.money.api.methods.RequestExternalPayment;
import com.yandex.money.api.methods.RequestPayment;
import com.yandex.money.api.model.MoneySource;
import com.yandex.money.api.net.DefaultApiClient;
import com.yandex.money.api.net.OAuth2Session;
import com.yandex.money.api.payment.BasePaymentProcess;
import com.yandex.money.api.payment.ExtendedPaymentProcess;
import com.yandex.money.api.payment.ExternalPaymentProcess;
import com.yandex.money.api.payment.PaymentProcess;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class PaymentProcessTest {

    @Test
    public void testPaymentProcessStateRestore() {
        BasePaymentProcess paymentProcess = new PaymentProcess(
                createOAuth2SessionStub(), createParameterProviderStub());

        BasePaymentProcess.SavedState savedState = createPaymentProcessSavedState();
        paymentProcess.restoreSavedState(savedState);
        BasePaymentProcess.SavedState state = paymentProcess.getSavedState();

        Assert.assertEquals(savedState.getRequestPayment(), state.getRequestPayment());
        Assert.assertEquals(savedState.getProcessPayment(), state.getProcessPayment());
        Assert.assertEquals(savedState.getFlags(), state.getFlags());
    }

    @Test
    public void testExternalPaymentProcessStateRestore() {
        BasePaymentProcess paymentProcess = new ExternalPaymentProcess(
                createOAuth2SessionStub(), createParameterProviderStub(), "");

        BasePaymentProcess.SavedState savedState = createExternalPaymentProcessSavedState();
        paymentProcess.restoreSavedState(savedState);
        BasePaymentProcess.SavedState state = paymentProcess.getSavedState();

        Assert.assertEquals(savedState.getRequestPayment(), state.getRequestPayment());
        Assert.assertEquals(savedState.getProcessPayment(), state.getProcessPayment());
        Assert.assertEquals(savedState.getFlags(), state.getFlags());
    }

    @Test
    public void testExtendedPaymentProcessStateRestore() {
        ExtendedPaymentProcess extendedPaymentProcess = new ExtendedPaymentProcess(
                createOAuth2SessionStub(), createParameterProviderStub(), "");

        ExtendedPaymentProcess.SavedState savedState = new ExtendedPaymentProcess.SavedState(
                createPaymentProcessSavedState(), createExternalPaymentProcessSavedState(), 11);
        extendedPaymentProcess.restoreSavedState(savedState);
        ExtendedPaymentProcess.SavedState state = extendedPaymentProcess.getSavedState();

        Assert.assertEquals(savedState.getFlags(), state.getFlags());
    }

    private OAuth2Session createOAuth2SessionStub() {
        return new OAuth2Session(new DefaultApiClient(""));
    }

    private BasePaymentProcess.ParameterProvider createParameterProviderStub() {
        return new BasePaymentProcess.ParameterProvider() {
            @Override
            public String getPatternId() {
                return null;
            }

            @Override
            public Map<String, String> getPaymentParameters() {
                return null;
            }

            @Override
            public MoneySource getMoneySource() {
                return null;
            }

            @Override
            public String getCsc() {
                return null;
            }

            @Override
            public String getExtAuthSuccessUri() {
                return null;
            }

            @Override
            public String getExtAuthFailUri() {
                return null;
            }
        };
    }

    private BasePaymentProcess.SavedState createPaymentProcessSavedState() {
        return new BasePaymentProcess.SavedState(
                new RequestPayment.Builder().createRequestPayment(),
                new ProcessPayment.Builder().createProcessPayment(),
                3
        );
    }

    private BasePaymentProcess.SavedState createExternalPaymentProcessSavedState() {
        return new BasePaymentProcess.SavedState(
                new RequestExternalPayment(null, null, null, null, null),
                new ProcessExternalPayment(null, null, null, new HashMap<String, String>(), null,
                        null, null), 3);
    }
}
