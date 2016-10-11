/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 NBCO Yandex.Money LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.yandex.money.api;

import com.yandex.money.api.methods.BaseProcessPayment;
import com.yandex.money.api.methods.BaseRequestPayment;
import com.yandex.money.api.methods.ProcessExternalPayment;
import com.yandex.money.api.methods.ProcessPayment;
import com.yandex.money.api.methods.RequestExternalPayment;
import com.yandex.money.api.methods.RequestPayment;
import com.yandex.money.api.model.MoneySource;
import com.yandex.money.api.net.clients.ApiClient;
import com.yandex.money.api.net.clients.DefaultApiClient;
import com.yandex.money.api.net.providers.DefaultApiV1HostsProvider;
import com.yandex.money.api.processes.BasePaymentProcess;
import com.yandex.money.api.processes.ExtendedPaymentProcess;
import com.yandex.money.api.processes.ExternalPaymentProcess;
import com.yandex.money.api.processes.PaymentProcess;
import com.yandex.money.api.util.HttpHeaders;
import com.yandex.money.api.util.MimeTypes;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
@Test(singleThreaded = true)
public class PaymentProcessTest {

    private final MockWebServer server = new MockWebServer();
    private final ApiClient client = new DefaultApiClient.Builder()
            .setClientId(ApiTest.CLIENT_ID)
            .setHostsProvider(new DefaultApiV1HostsProvider(false) {
                @Override
                public String getMoney() {
                    return server.url("").toString();
                }
            })
            .create();
    private final ExternalPaymentProcess.ParameterProvider parameterProvider =
            createParameterProviderStub();

    @BeforeClass
    public void setUp() throws IOException {
        server.start();
    }

    @Test
    public void testPaymentProcess() throws Exception {
        enqueuePaymentProcess();
        checkPaymentProcess(new PaymentProcess(client, parameterProvider));
    }

    @Test
    public void testAsyncPaymentProcess() throws Exception {
        enqueuePaymentProcess();
        checkAsyncPaymentProcess(new PaymentProcess(client, parameterProvider));
    }

    @Test
    public void testExternalPaymentProcess() throws Exception {
        enqueueExternalPaymentProcess();
        ExternalPaymentProcess process = new ExternalPaymentProcess(client, parameterProvider);
        process.setInstanceId("instanceId");
        checkPaymentProcess(process);
    }

    @Test
    public void testAsyncExternalPaymentProcess() throws Exception {
        enqueueExternalPaymentProcess();
        ExternalPaymentProcess process = new ExternalPaymentProcess(client, parameterProvider);
        process.setInstanceId("instanceId");
        checkAsyncPaymentProcess(process);
    }

    @Test
    public void testPaymentProcessStateRestore() {
        PaymentProcess paymentProcess = new PaymentProcess(client, parameterProvider);

        BasePaymentProcess.SavedState<RequestPayment, ProcessPayment> savedState =
                createPaymentProcessSavedState();
        paymentProcess.restoreSavedState(savedState);
        BasePaymentProcess.SavedState state = paymentProcess.getSavedState();

        Assert.assertEquals(savedState.getRequestPayment(), state.getRequestPayment());
        Assert.assertEquals(savedState.getProcessPayment(), state.getProcessPayment());
        Assert.assertEquals(savedState.getFlags(), state.getFlags());
    }

    @Test
    public void testExternalPaymentProcessStateRestore() {
        ExternalPaymentProcess paymentProcess = new ExternalPaymentProcess(client, parameterProvider);

        BasePaymentProcess.SavedState<RequestExternalPayment, ProcessExternalPayment> savedState =
                createExternalPaymentProcessSavedState();
        paymentProcess.restoreSavedState(savedState);
        BasePaymentProcess.SavedState state = paymentProcess.getSavedState();

        Assert.assertEquals(savedState.getRequestPayment(), state.getRequestPayment());
        Assert.assertEquals(savedState.getProcessPayment(), state.getProcessPayment());
        Assert.assertEquals(savedState.getFlags(), state.getFlags());
    }

    @Test
    public void testExtendedPaymentProcessStateRestore() {
        ExtendedPaymentProcess extendedPaymentProcess = new ExtendedPaymentProcess(
                client, parameterProvider);

        ExtendedPaymentProcess.SavedState savedState = new ExtendedPaymentProcess.SavedState(
                createPaymentProcessSavedState(), createExternalPaymentProcessSavedState(), 11);
        extendedPaymentProcess.restoreSavedState(savedState);
        ExtendedPaymentProcess.SavedState state = extendedPaymentProcess.getSavedState();

        Assert.assertEquals(savedState.getFlags(), state.getFlags());
    }

    private void enqueuePaymentProcess() {
        enqueueResponse("{\n" +
                "    \"status\": \"success\",\n" +
                "    \"contract\": \"\",\n" +
                "    \"balance\": 8.09,\n" +
                "    \"request_id\": " +
                "\"313938393033343938345f393436313038303263393537373537626365313563643232303061616165616431653338393438395f3438383132373336\",\n" +
                "    \"contract_amount\": 1,\n" +
                "    \"money_source\": {\n" +
                "        \"cards\": {\n" +
                "            \"allowed\": false\n" +
                "        },\n" +
                "        \"wallet\": {\n" +
                "            \"allowed\": true\n" +
                "        },\n" +
                "        \"card\": {\n" +
                "            \"allowed\": \"false\"\n" +
                "        }\n" +
                "    }\n" +
                "}");
        enqueueResponse("{\"status\":\"in_progress\",\"next_retry\":1}");
        enqueueResponse("{\"status\":\"success\",\"payment_id\":\"2ABCDE123456789\",\"invoice_id\":\"1234567890123456789\",\"balance\":1000}");
    }

    private void enqueueExternalPaymentProcess() {
        enqueueResponse("{\"status\":\"success\",\"request_id\":\"3931303833373438395f343434316466313864616236613236363063386361663834336137386537643935383639383062635f3330363333323938\",\"contract_amount\":100,\"title\":\"Оплата услуг NNNN\"}");
        enqueueResponse("{\"status\":\"in_progress\",\"next_retry\":1}");
        enqueueResponse("{\"status\":\"success\",\"invoice_id\":\"3000130505460\",\"money_source\":{\"type\":\"payment-card\",\"payment_card_type\":\"VISA\",\"pan_fragment\":\"**** **** **** 0334\",\"money_source_token\":\"B6AE719BAF712404E08EF8A430B0F58CD8F2C592452CA5205F7E52B1FC72BD3D42745714D60B4E75BD742F22E8120F0861ED99B69EC01C6194CF5D425C89598B959DE0E9EDB13AFD710CF74ACE08DBFBE2A49F14F9792B32289CE2456EB50EF7DFE6D22E466D417ACD1BF8DE33B5C93BDA9AAA8C4D693DCD2E9AA2A31A51C185\"}}");
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

    private synchronized void checkAsyncPaymentProcess(BasePaymentProcess process)
            throws Exception {

        process.proceed();
        process.proceed();
    }

    private ExternalPaymentProcess.ParameterProvider createParameterProviderStub() {
        return new ExternalPaymentProcess.ParameterProvider() {
            @Override
            public String getPatternId() {
                return "1234";
            }

            @Override
            public Map<String, String> getPaymentParameters() {
                final HashMap<String, String> params = new HashMap<>();
                params.put("foo", "bar");
                return params;
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

            @Override
            public boolean isRequestToken() {
                return false;
            }
        };
    }

    private PaymentProcess.SavedState createPaymentProcessSavedState() {
        return new PaymentProcess.SavedState(
                (RequestPayment) new RequestPayment.Builder()
                        .setMoneySources(Collections.<MoneySource>emptyList())
                        .setBalance(BigDecimal.TEN)
                        .setStatus(BaseRequestPayment.Status.SUCCESS)
                        .setContractAmount(BigDecimal.ONE)
                        .setRequestId("1234567890")
                        .create(),
                (ProcessPayment) new ProcessPayment.Builder()
                        .setPaymentId("12346890")
                        .setBalance(BigDecimal.TEN)
                        .setStatus(BaseProcessPayment.Status.SUCCESS)
                        .setAcsParams(Collections.<String, String>emptyMap())
                        .setInvoiceId("1234567890")
                        .create(),
                3
        );
    }

    private ExternalPaymentProcess.SavedState createExternalPaymentProcessSavedState() {
        return new ExternalPaymentProcess.SavedState(
                (RequestExternalPayment) new RequestExternalPayment.Builder()
                        .setStatus(BaseRequestPayment.Status.SUCCESS)
                        .setContractAmount(BigDecimal.ONE)
                        .setRequestId("1234567890")
                        .create(),
                (ProcessExternalPayment) new ProcessExternalPayment.Builder()
                        .setAcsParams(Collections.<String, String>emptyMap())
                        .setStatus(BaseProcessPayment.Status.SUCCESS)
                        .setInvoiceId("1234567890")
                        .create(),
                3
        );
    }
}
