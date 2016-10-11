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
import com.yandex.money.api.methods.InstanceId;
import com.yandex.money.api.methods.ProcessExternalPayment;
import com.yandex.money.api.methods.ProcessPayment;
import com.yandex.money.api.methods.RequestExternalPayment;
import com.yandex.money.api.methods.RequestPayment;
import com.yandex.money.api.methods.params.PhoneParams;
import com.yandex.money.api.model.Error;
import com.yandex.money.api.model.SimpleStatus;
import com.yandex.money.api.net.ApiClient;
import com.yandex.money.api.net.ApiRequest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 *
 */
public class YandexMoneyTest implements ApiTest {

    private final String phoneNumber = LOCAL_PROPERTIES.getPhoneNumber();
    private final BigDecimal amount = LOCAL_PROPERTIES.getAmount();

    private ApiClient client;

    private InstanceId respInstanceId;
    private InstanceId.Request reqInstanceId;

    private RequestExternalPayment respRequestExternalPayment;
    private RequestExternalPayment.Request reqRequestExternalPayment;
    private ProcessExternalPayment.Request reqProcessExternalPayment;

    @Test
    public void testInstanceIdSuccess() throws Exception {
        reqInstanceId = new InstanceId.Request(CLIENT_ID);
        respInstanceId = client.execute(reqInstanceId);

        Assert.assertEquals(respInstanceId.statusInfo.status, SimpleStatus.SUCCESS);
        Assert.assertNotNull(respInstanceId.instanceId);
        Assert.assertNull(respInstanceId.statusInfo.error);
    }

    @Test
    public void testInstanceIdFail() throws Exception {
        reqInstanceId = new InstanceId.Request(" ");
        respInstanceId = client.execute(reqInstanceId);

        Assert.assertEquals(respInstanceId.statusInfo.status, SimpleStatus.REFUSED);
        Assert.assertNotNull(respInstanceId.statusInfo.error);
        Assert.assertEquals(respInstanceId.statusInfo.error, Error.ILLEGAL_PARAM_CLIENT_ID);
        Assert.assertNull(respInstanceId.instanceId);
    }

    @Test
    public void testRequestExternalPayment() throws Exception {
        reqRequestExternalPayment = createRequestExternalPayment();
        respRequestExternalPayment = testRequestPayment(reqRequestExternalPayment);
    }

    @Test
    public void testRequestPayment() throws Exception {
        client.setAccessToken(ACCESS_TOKEN);
        testRequestPayment(createRequestPayment());
        client.setAccessToken(null);
    }

    @Test
    public void testRequestExternalFail() throws Exception {
        reqInstanceId = new InstanceId.Request(CLIENT_ID);
        respInstanceId = client.execute(reqInstanceId);

        HashMap<String, String> params = successRequestParams();
        reqRequestExternalPayment = RequestExternalPayment.Request.newInstance(
                respInstanceId.instanceId, " ", params);
        respRequestExternalPayment = testRequestPaymentFail(reqRequestExternalPayment);

        params = successRequestParams();
        params.remove("amount");
        reqRequestExternalPayment = RequestExternalPayment.Request.newInstance(
                respInstanceId.instanceId, PhoneParams.PATTERN_ID, params);
        respRequestExternalPayment = testRequestPaymentFail(reqRequestExternalPayment);

        params = successRequestParams();
        params.remove("phone-number");
        reqRequestExternalPayment = RequestExternalPayment.Request.newInstance(
                respInstanceId.instanceId, PhoneParams.PATTERN_ID, params);
        respRequestExternalPayment = testRequestPaymentFail(reqRequestExternalPayment);
    }

    @Test
    public void testProcessExternalPayment() throws Exception {
        reqRequestExternalPayment = createRequestExternalPayment();
        respRequestExternalPayment = client.execute(reqRequestExternalPayment);

        if (respRequestExternalPayment.status == BaseRequestPayment.Status.SUCCESS) {
            String successUri = "https://elbrus.yandex.ru/success";
            String failUri = "https://elbrus.yandex.ru/fail";
            reqProcessExternalPayment = new ProcessExternalPayment.Request(
                    respInstanceId.instanceId, respRequestExternalPayment.requestId,
                    successUri, failUri, false);
            testProcessPayment(reqProcessExternalPayment);
        }
    }

    @Test
    public void testProcessPayment() throws Exception {
        client.setAccessToken(ACCESS_TOKEN);
        RequestPayment requestPayment = client.execute(createRequestPayment());
        if (requestPayment.status == BaseRequestPayment.Status.SUCCESS) {
            ProcessPayment processPayment = client.execute(
                    new ProcessPayment.Request(requestPayment.requestId)
                            .setTestResult(ProcessPayment.TestResult.SUCCESS));
            Assert.assertNotNull(processPayment);
        } else {
            if (requestPayment.error != Error.TECHNICAL_ERROR) {
                Assert.assertEquals(requestPayment.error, Error.NOT_ENOUGH_FUNDS);
            }
        }
        client.setAccessToken(null);
    }

    @BeforeClass
    private void setUp() {
        client = DEFAULT_API_CLIENT_BUILDER.create();
    }

    @BeforeTest
    private void beforeTest() {
        reqInstanceId = null;
        respInstanceId = null;

        reqRequestExternalPayment = null;
        respRequestExternalPayment = null;

        reqProcessExternalPayment = null;
    }

    private <T extends BaseRequestPayment> T testRequestPayment(ApiRequest<T> request) throws Exception {
        T response = client.execute(request);

        Assert.assertNotNull(response);
        if (response.status == BaseRequestPayment.Status.SUCCESS) {
            Assert.assertEquals(response.status, BaseRequestPayment.Status.SUCCESS);
            Assert.assertNull(response.error);
            Assert.assertNotNull(response.requestId);
            Assert.assertTrue(response.requestId.length() > 0);
            Assert.assertEquals(response.contractAmount.compareTo(amount), 0);
        } else {
            if (response.error != Error.TECHNICAL_ERROR) {
                Assert.assertEquals(response.error, Error.NOT_ENOUGH_FUNDS);
            }
        }

        return response;
    }

    private HashMap<String, String> successRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("amount", amount.toPlainString());
        params.put("phone-number", phoneNumber);
        return params;
    }

    private <T extends BaseRequestPayment> T testRequestPaymentFail(ApiRequest<T> request) throws Exception {
        T response = client.execute(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.status, RequestExternalPayment.Status.REFUSED);
        Assert.assertNotNull(response.error);
        Assert.assertNotEquals(response.error, Error.UNKNOWN);
        Assert.assertNull(response.requestId);

        return response;
    }

    private <T extends BaseProcessPayment> void testProcessPayment(ApiRequest<T> request) throws Exception {
        T response = client.execute(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.status, ProcessExternalPayment.Status.EXT_AUTH_REQUIRED);
    }

    private RequestExternalPayment.Request createRequestExternalPayment() throws Exception {
        reqInstanceId = new InstanceId.Request(CLIENT_ID);
        respInstanceId = client.execute(reqInstanceId);

        HashMap<String, String> params = successRequestParams();
        return RequestExternalPayment.Request.newInstance(
                respInstanceId.instanceId, PhoneParams.PATTERN_ID, params);
    }

    private RequestPayment.Request createRequestPayment() {
        return RequestPayment.Request.newInstance(PhoneParams.newInstance(phoneNumber, amount))
                .setTestResult(RequestPayment.TestResult.SUCCESS);
    }
}
