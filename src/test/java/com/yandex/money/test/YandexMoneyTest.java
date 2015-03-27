package com.yandex.money.test;

import com.yandex.money.api.exceptions.InsufficientScopeException;
import com.yandex.money.api.exceptions.InvalidRequestException;
import com.yandex.money.api.exceptions.InvalidTokenException;
import com.yandex.money.api.methods.BaseProcessPayment;
import com.yandex.money.api.methods.BaseRequestPayment;
import com.yandex.money.api.methods.InstanceId;
import com.yandex.money.api.methods.ProcessExternalPayment;
import com.yandex.money.api.methods.ProcessPayment;
import com.yandex.money.api.methods.RequestExternalPayment;
import com.yandex.money.api.methods.RequestPayment;
import com.yandex.money.api.methods.params.PhoneParams;
import com.yandex.money.api.model.Error;
import com.yandex.money.api.net.DefaultApiClient;
import com.yandex.money.api.net.MethodRequest;
import com.yandex.money.api.net.OAuth2Session;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;

/**
 *
 */
public class YandexMoneyTest implements ApiTest {

    private static final String PATTERN_ID_PHONE_TOPUP = PhoneParams.PATTERN_ID;
    private static final String AMOUNT = "1.00";

    private OAuth2Session session;

    private InstanceId respInstanceId;
    private InstanceId.Request reqInstanceId;

    private RequestExternalPayment respRequestExternalPayment;
    private RequestExternalPayment.Request reqRequestExternalPayment;
    private ProcessExternalPayment.Request reqProcessExternalPayment;

    @BeforeClass
    private void setUp() {
        session = new OAuth2Session(new DefaultApiClient(CLIENT_ID));
        session.setDebugLogging(true);
    }

    @BeforeTest
    private void beforeTest() {
        reqInstanceId = null;
        respInstanceId = null;

        reqRequestExternalPayment = null;
        respRequestExternalPayment = null;

        reqProcessExternalPayment = null;
    }

    @Test
    public void testInstanceIdSuccess() throws IOException, InsufficientScopeException,
            InvalidTokenException, InvalidRequestException {

        reqInstanceId = new InstanceId.Request(CLIENT_ID);
        respInstanceId = session.execute(reqInstanceId);

        Assert.assertEquals(respInstanceId.status, InstanceId.Status.SUCCESS);
        Assert.assertNotNull(respInstanceId.instanceId);
        Assert.assertNull(respInstanceId.error);
    }

    @Test
    public void testInstanceIdFail() throws IOException, InsufficientScopeException,
            InvalidTokenException, InvalidRequestException {

        reqInstanceId = new InstanceId.Request(" ");
        respInstanceId = session.execute(reqInstanceId);

        Assert.assertEquals(respInstanceId.status, InstanceId.Status.REFUSED);
        Assert.assertNotNull(respInstanceId.error);
        Assert.assertEquals(respInstanceId.error, Error.ILLEGAL_PARAM_CLIENT_ID);
        Assert.assertNull(respInstanceId.instanceId);
    }

    @Test
    public void testRequestExternalPayment() throws IOException, InsufficientScopeException,
            InvalidTokenException, InvalidRequestException {

        reqRequestExternalPayment = createRequestExternalPayment();
        respRequestExternalPayment = testRequestPayment(reqRequestExternalPayment);
    }

    @Test
    public void testRequestPayment() throws InvalidTokenException, InsufficientScopeException,
            InvalidRequestException, IOException {

        session.setAccessToken(ACCESS_TOKEN);
        testRequestPayment(createRequestPayment());
        session.setAccessToken(null);
    }

    private <T extends BaseRequestPayment> T testRequestPayment(MethodRequest<T> request)
            throws InvalidTokenException, InsufficientScopeException, InvalidRequestException,
            IOException {

        T response = session.execute(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.status, BaseRequestPayment.Status.SUCCESS);
        Assert.assertNull(response.error);
        Assert.assertNotNull(response.requestId);
        Assert.assertTrue(response.requestId.length() > 0);
        Assert.assertEquals(response.contractAmount, new BigDecimal(AMOUNT));

        return response;
    }

    private HashMap<String, String> successRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("amount", AMOUNT);
        params.put("phone-number", "79112611383");
        return params;
    }

    @Test
    public void testRequestExternalFail() throws IOException, InsufficientScopeException,
            InvalidTokenException, InvalidRequestException {

        reqInstanceId = new InstanceId.Request(CLIENT_ID);
        respInstanceId = session.execute(reqInstanceId);

        HashMap<String, String> params = successRequestParams();
        reqRequestExternalPayment = RequestExternalPayment.Request.newInstance(
                respInstanceId.instanceId, " ", params);
        respRequestExternalPayment = testRequestPaymentFail(reqRequestExternalPayment);

        params = successRequestParams();
        params.remove("amount");
        reqRequestExternalPayment = RequestExternalPayment.Request.newInstance(
                respInstanceId.instanceId, PATTERN_ID_PHONE_TOPUP, params);
        respRequestExternalPayment = testRequestPaymentFail(reqRequestExternalPayment);

        params = successRequestParams();
        params.remove("phone-number");
        reqRequestExternalPayment = RequestExternalPayment.Request.newInstance(
                respInstanceId.instanceId, PATTERN_ID_PHONE_TOPUP, params);
        respRequestExternalPayment = testRequestPaymentFail(reqRequestExternalPayment);
    }

    private <T extends BaseRequestPayment> T testRequestPaymentFail(MethodRequest<T> request)
            throws InvalidTokenException, InsufficientScopeException, InvalidRequestException,
            IOException {

        T response = session.execute(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.status, RequestExternalPayment.Status.REFUSED);
        Assert.assertNotNull(response.error);
        Assert.assertNotEquals(response.error, Error.UNKNOWN);
        Assert.assertNull(response.requestId);

        return response;
    }

    @Test
    public void testProcessExternalPayment() throws IOException, InsufficientScopeException,
            InvalidTokenException, InvalidRequestException {

        reqRequestExternalPayment = createRequestExternalPayment();
        respRequestExternalPayment = session.execute(reqRequestExternalPayment);

        String successUri = "https://elbrus.yandex.ru/success";
        String failUri = "https://elbrus.yandex.ru/fail";
        reqProcessExternalPayment = new ProcessExternalPayment.Request(
                respInstanceId.instanceId, respRequestExternalPayment.requestId,
                successUri, failUri, false);
        testProcessPayment(reqProcessExternalPayment);
    }

    @Test
    public void testProcessPayment() throws InvalidTokenException, InsufficientScopeException,
            InvalidRequestException, IOException {

        session.setAccessToken(ACCESS_TOKEN);
        RequestPayment requestPayment = session.execute(createRequestPayment());
        ProcessPayment processPayment = session.execute(new ProcessPayment.Request(
                requestPayment.requestId));
        Assert.assertNotNull(processPayment);
        session.setAccessToken(null);
    }

    private <T extends BaseProcessPayment> void testProcessPayment(MethodRequest<T> request)
            throws InvalidTokenException, InsufficientScopeException, InvalidRequestException,
            IOException {

        T response = session.execute(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(response.status, ProcessExternalPayment.Status.EXT_AUTH_REQUIRED);
    }

    private RequestExternalPayment.Request createRequestExternalPayment()
            throws InvalidTokenException, InsufficientScopeException, InvalidRequestException,
            IOException {

        reqInstanceId = new InstanceId.Request(CLIENT_ID);
        respInstanceId = session.execute(reqInstanceId);

        HashMap<String, String> params = successRequestParams();
        return RequestExternalPayment.Request.newInstance(
                respInstanceId.instanceId, PATTERN_ID_PHONE_TOPUP, params);
    }

    private RequestPayment.Request createRequestPayment() {
        HashMap<String, String> params = successRequestParams();
        return new RequestPayment.Request(params.get("phone-number"), new BigDecimal(AMOUNT));
    }
}
