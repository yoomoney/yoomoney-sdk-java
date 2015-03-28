package com.yandex.money.test;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import com.yandex.money.api.methods.ProcessExternalPayment;
import com.yandex.money.api.methods.ProcessPayment;
import com.yandex.money.api.methods.RequestExternalPayment;
import com.yandex.money.api.methods.RequestPayment;
import com.yandex.money.api.model.MoneySource;
import com.yandex.money.api.net.DefaultApiClient;
import com.yandex.money.api.net.HostsProvider;
import com.yandex.money.api.net.OAuth2Session;
import com.yandex.money.api.processes.BasePaymentProcess;
import com.yandex.money.api.processes.ExtendedPaymentProcess;
import com.yandex.money.api.processes.ExternalPaymentProcess;
import com.yandex.money.api.processes.PaymentProcess;
import com.yandex.money.api.utils.HttpHeaders;
import com.yandex.money.api.utils.MimeTypes;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class PaymentProcessTest {

    private final MockWebServer server = new MockWebServer();
    private final OAuth2Session session = new OAuth2Session(new DefaultApiClient("stub", false) {
        @Override
        public HostsProvider getHostsProvider() {
            return new HostsProvider(false) {
                @Override
                public String getMoney() {
                    return server.getUrl("").toString();
                }
            };
        }
    });
    private final BasePaymentProcess.ParameterProvider parameterProvider =
            createParameterProviderStub();

    @BeforeClass
    public void setUp() throws IOException {
        server.start();
    }

    @Test
    public void testPaymentProcess() throws Exception {
        enqueueResponse("{\"status\":\"success\",\"wallet\":{\"allowed\":true},\"card\":{\"allowed\":\"true\",\"csc_required\":\"true\",\"id\":\"card-385244400\",\"pan_fragment\":\"5280****7918\",\"type\":\"MasterCard\"},\"cards\":{\"allowed\":true,\"csc_required\":true,\"items\":[{\"id\":\"card-385244400\",\"pan_fragment\":\"5280****7918\",\"type\":\"MasterCard\"},{\"id\":\"card-385244401\",\"pan_fragment\":\"4008****7919\",\"type\":\"Visa\"}]},\"request_id\":\"33373230335f343462363963333932636234633130613062623338323265393136323530336564636130623263375f33303030333539313637\",\"balance\":1000}");
        enqueueResponse("{\"status\":\"in_progress\",\"next_retry\":1}");
        enqueueResponse("{\"status\":\"success\",\"payment_id\":\"2ABCDE123456789\",\"invoice_id\":\"1234567890123456789\",\"balance\":1000}");
        checkPaymentProcess(new PaymentProcess(session, parameterProvider));
    }

    @Test
    public void testExternalPaymentProcess() throws Exception {
        enqueueResponse("{\"status\":\"success\",\"request_id\":\"3931303833373438395f343434316466313864616236613236363063386361663834336137386537643935383639383062635f3330363333323938\",\"contract_amount\":100,\"title\":\"Оплата услуг NNNN\"}");
        enqueueResponse("{\"status\":\"in_progress\",\"next_retry\":1}");
        enqueueResponse("{\"status\":\"success\",\"invoice_id\":\"3000130505460\",\"money_source\":{\"type\":\"payment-card\",\"payment_card_type\":\"VISA\",\"pan_fragment\":\"**** **** **** 0334\",\"money_source_token\":\"B6AE719BAF712404E08EF8A430B0F58CD8F2C592452CA5205F7E52B1FC72BD3D42745714D60B4E75BD742F22E8120F0861ED99B69EC01C6194CF5D425C89598B959DE0E9EDB13AFD710CF74ACE08DBFBE2A49F14F9792B32289CE2456EB50EF7DFE6D22E466D417ACD1BF8DE33B5C93BDA9AAA8C4D693DCD2E9AA2A31A51C185\"}}");
        checkPaymentProcess(new ExternalPaymentProcess(session, parameterProvider, "stub"));
    }

    @Test
    public void testPaymentProcessStateRestore() {
        BasePaymentProcess paymentProcess = new PaymentProcess(session, parameterProvider);

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
                session, parameterProvider, "");

        BasePaymentProcess.SavedState savedState = createExternalPaymentProcessSavedState();
        paymentProcess.restoreSavedState(savedState);
        BasePaymentProcess.SavedState state = paymentProcess.getSavedState();

        Assert.assertEquals(savedState.getRequestPayment(), state.getRequestPayment());
        Assert.assertEquals(savedState.getProcessPayment(), state.getProcessPayment());
        Assert.assertEquals(savedState.getFlags(), state.getFlags());
    }

    @Test
    public void testExtendedPaymentProcessStateRestore() {
        ExtendedPaymentProcess extendedPaymentProcess = new ExtendedPaymentProcess(session,
                parameterProvider, "");

        ExtendedPaymentProcess.SavedState savedState = new ExtendedPaymentProcess.SavedState(
                createPaymentProcessSavedState(), createExternalPaymentProcessSavedState(), 11);
        extendedPaymentProcess.restoreSavedState(savedState);
        ExtendedPaymentProcess.SavedState state = extendedPaymentProcess.getSavedState();

        Assert.assertEquals(savedState.getFlags(), state.getFlags());
    }

    private void enqueueResponse(String response) {
        server.enqueue(new MockResponse()
                .addHeader(HttpHeaders.CONTENT_TYPE, MimeTypes.Application.JSON)
                .setBody(response));
    }

    private void checkPaymentProcess(BasePaymentProcess process) throws Exception {
        Assert.assertFalse(process.proceed());
        Assert.assertTrue(process.proceed());
    }

    private BasePaymentProcess.ParameterProvider createParameterProviderStub() {
        return new BasePaymentProcess.ParameterProvider() {
            @Override
            public String getPatternId() {
                return "1234";
            }

            @Override
            public Map<String, String> getPaymentParameters() {
                return new HashMap<>();
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
                return "stub";
            }

            @Override
            public String getExtAuthFailUri() {
                return "stub";
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
