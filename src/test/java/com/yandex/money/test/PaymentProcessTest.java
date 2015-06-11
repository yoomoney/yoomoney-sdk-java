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
@Test(singleThreaded = true)
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
    private final ExternalPaymentProcess.ParameterProvider parameterProvider =
            createParameterProviderStub();

    @BeforeClass
    public void setUp() throws IOException {
        server.start();
    }

    @Test
    public void testPaymentProcess() throws Exception {
        enqueuePaymentProcess();
        checkPaymentProcess(new PaymentProcess(session, parameterProvider));
    }

    @Test
    public void testAsyncPaymentProcess() throws Exception {
        enqueuePaymentProcess();
        checkAsyncPaymentProcess(new PaymentProcess(session, parameterProvider));
    }

    @Test
    public void testExternalPaymentProcess() throws Exception {
        enqueueExternalPaymentProcess();
        ExternalPaymentProcess process = new ExternalPaymentProcess(session, parameterProvider);
        process.setInstanceId("instanceId");
        checkPaymentProcess(process);
    }

    @Test
    public void testAsyncExternalPaymentProcess() throws Exception {
        enqueueExternalPaymentProcess();
        ExternalPaymentProcess process = new ExternalPaymentProcess(session, parameterProvider);
        process.setInstanceId("instanceId");
        checkAsyncPaymentProcess(process);
    }

    @Test
    public void testPaymentProcessStateRestore() {
        PaymentProcess paymentProcess = new PaymentProcess(session, parameterProvider);

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
        ExternalPaymentProcess paymentProcess = new ExternalPaymentProcess(
                session, parameterProvider);

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
                session, parameterProvider);

        ExtendedPaymentProcess.SavedState savedState = new ExtendedPaymentProcess.SavedState(
                createPaymentProcessSavedState(), createExternalPaymentProcessSavedState(), 11);
        extendedPaymentProcess.restoreSavedState(savedState);
        ExtendedPaymentProcess.SavedState state = extendedPaymentProcess.getSavedState();

        Assert.assertEquals(savedState.getFlags(), state.getFlags());
    }

    private void enqueuePaymentProcess() {
        enqueueResponse("{\"status\":\"success\",\"wallet\":{\"allowed\":true},\"card\":{\"allowed\":\"true\",\"csc_required\":\"true\",\"id\":\"card-385244400\",\"pan_fragment\":\"5280****7918\",\"type\":\"MasterCard\"},\"cards\":{\"allowed\":true,\"csc_required\":true,\"items\":[{\"id\":\"card-385244400\",\"pan_fragment\":\"5280****7918\",\"type\":\"MasterCard\"},{\"id\":\"card-385244401\",\"pan_fragment\":\"4008****7919\",\"type\":\"Visa\"}]},\"request_id\":\"33373230335f343462363963333932636234633130613062623338323265393136323530336564636130623263375f33303030333539313637\",\"balance\":1000}");
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

        ThreadSync sync = new ThreadSync();

        //noinspection unchecked
        process.setCallbacks(new Callbacks(sync));

        process.proceedAsync();
        sync.doWait();

        process.proceedAsync();
        sync.doWait();
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
                new RequestPayment.Builder().createRequestPayment(),
                new ProcessPayment.Builder().createProcessPayment(),
                3
        );
    }

    private ExternalPaymentProcess.SavedState createExternalPaymentProcessSavedState() {
        return new ExternalPaymentProcess.SavedState(
                new RequestExternalPayment(null, null, null, null, null),
                new ProcessExternalPayment(null, null, null, null, new HashMap<String, String>(),
                        null, null),
                3
        );
    }

    private static final class Callbacks implements BasePaymentProcess.Callbacks {

        private final ThreadSync sync;
        private final OnResponseReady onResponseReady = new OnResponseReady();

        public Callbacks(ThreadSync sync) {
            this.sync = sync;
        }

        @Override
        public com.yandex.money.api.net.OnResponseReady getOnRequestCallback() {
            return onResponseReady;
        }

        @Override
        public com.yandex.money.api.net.OnResponseReady getOnProcessCallback() {
            return onResponseReady;
        }

        private final class OnResponseReady implements com.yandex.money.api.net.OnResponseReady {
            @Override
            public void onFailure(Exception exception) {
                Assert.assertTrue(false);
                sync.doWait();
            }

            @Override
            public void onResponse(Object response) {
                Assert.assertTrue(true);
                sync.doNotify();
            }
        }
    }
}
