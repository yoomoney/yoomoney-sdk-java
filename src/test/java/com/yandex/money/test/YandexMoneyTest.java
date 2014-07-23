package com.yandex.money.test;

import com.yandex.money.YandexMoney;
import com.yandex.money.exceptions.InsufficientScopeException;
import com.yandex.money.exceptions.InvalidRequestException;
import com.yandex.money.exceptions.InvalidTokenException;
import com.yandex.money.model.common.params.PhoneParams;
import com.yandex.money.model.cps.BaseRequestPayment;
import com.yandex.money.model.cps.Error;
import com.yandex.money.model.cps.InstanceId;
import com.yandex.money.model.cps.ProcessExternalPayment;
import com.yandex.money.model.cps.RequestExternalPayment;
import com.yandex.money.model.cps.RequestPayment;
import com.yandex.money.net.MethodRequest;

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

    private YandexMoney ym;

    private InstanceId respInstanceId;
    private InstanceId.Request reqInstanceId;

    private RequestExternalPayment respRequestExternalPayment;
    private RequestExternalPayment.Request reqRequestExternalPayment;

    private ProcessExternalPayment respProcessExternalPayment;
    private ProcessExternalPayment.Request reqProcessExternalPayment;

    @BeforeClass
    private void setUp() {
        ym = new YandexMoney();
        ym.setDebugLogging(true);
    }

    @BeforeTest
    private void beforeTest() {
        reqInstanceId = null;
        respInstanceId = null;

        reqRequestExternalPayment = null;
        respRequestExternalPayment = null;

        reqProcessExternalPayment = null;
        respProcessExternalPayment = null;
    }

    @Test
    public void testInstanceIdSuccess() throws IOException, InsufficientScopeException,
            InvalidTokenException, InvalidRequestException {

        reqInstanceId = new InstanceId.Request(CLIENT_ID);
        respInstanceId = ym.execute(reqInstanceId);

        Assert.assertEquals(respInstanceId.getStatus(), InstanceId.Status.SUCCESS);
        Assert.assertNotNull(respInstanceId.getInstanceId());
        Assert.assertNull(respInstanceId.getError());
    }

    @Test
    public void testInstanceIdFail() throws IOException, InsufficientScopeException,
            InvalidTokenException, InvalidRequestException {

        reqInstanceId = new InstanceId.Request(" ");
        respInstanceId = ym.execute(reqInstanceId);

        Assert.assertEquals(respInstanceId.getStatus(), InstanceId.Status.REFUSED);
        Assert.assertNotNull(respInstanceId.getError());
        Assert.assertEquals(respInstanceId.getError(), Error.ILLEGAL_PARAM_CLIENT_ID);
        Assert.assertNull(respInstanceId.getInstanceId());
    }

    @Test
    public void testRequestExternalPayment() throws IOException, InsufficientScopeException,
            InvalidTokenException, InvalidRequestException {

        reqInstanceId = new InstanceId.Request(CLIENT_ID);
        respInstanceId = ym.execute(reqInstanceId);

        String patternId = "phone-topup";
        HashMap<String, String> params = successRequestParams();
        reqRequestExternalPayment = RequestExternalPayment.Request.newInstance(
                respInstanceId.getInstanceId(), patternId, params);

        respRequestExternalPayment = testRequestPayment(reqRequestExternalPayment);
    }

    @Test
    public void testRequestPayment() throws InvalidTokenException, InsufficientScopeException,
            InvalidRequestException, IOException {

        HashMap<String, String> params = successRequestParams();
        RequestPayment.Request request = new RequestPayment.Request(
                params.get("phone-number"), new BigDecimal(AMOUNT));
        ym.setAccessToken(ACCESS_TOKEN);
        testRequestPayment(request);
        ym.setAccessToken(null);
    }

    private <T extends BaseRequestPayment> T testRequestPayment(MethodRequest<T> request)
            throws InvalidTokenException, InsufficientScopeException, InvalidRequestException,
            IOException {

        T response = ym.execute(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatus(), BaseRequestPayment.Status.SUCCESS);
        Assert.assertNull(response.getError());
        Assert.assertNotNull(response.getRequestId());
        Assert.assertTrue(response.getRequestId().length() > 0);
        Assert.assertEquals(response.getContractAmount(), new BigDecimal(AMOUNT));

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
        respInstanceId = ym.execute(reqInstanceId);

        HashMap<String, String> params = successRequestParams();
        reqRequestExternalPayment = RequestExternalPayment.Request.newInstance(
                respInstanceId.getInstanceId(), " ", params);
        respRequestExternalPayment = testRequestPaymentFail(reqRequestExternalPayment);

        params = successRequestParams();
        params.remove("amount");
        reqRequestExternalPayment = RequestExternalPayment.Request.newInstance(
                respInstanceId.getInstanceId(), PATTERN_ID_PHONE_TOPUP, params);
        respRequestExternalPayment = testRequestPaymentFail(reqRequestExternalPayment);

        params = successRequestParams();
        params.remove("phone-number");
        reqRequestExternalPayment = RequestExternalPayment.Request.newInstance(
                respInstanceId.getInstanceId(), PATTERN_ID_PHONE_TOPUP, params);
        respRequestExternalPayment = testRequestPaymentFail(reqRequestExternalPayment);
    }

    private <T extends BaseRequestPayment> T testRequestPaymentFail(MethodRequest<T> request)
            throws InvalidTokenException, InsufficientScopeException, InvalidRequestException,
            IOException {

        T response = ym.execute(request);

        Assert.assertNotNull(response);
        Assert.assertEquals(response.getStatus(), RequestExternalPayment.Status.REFUSED);
        Assert.assertNotNull(response.getError());
        Assert.assertNotEquals(response.getError(), Error.UNKNOWN);
        Assert.assertNull(response.getRequestId());

        return response;
    }

    @Test
    public void testProcessExternalPayment() throws IOException, InsufficientScopeException,
            InvalidTokenException, InvalidRequestException {

        reqInstanceId = new InstanceId.Request(CLIENT_ID);
        respInstanceId = ym.execute(reqInstanceId);

        HashMap<String, String> params = successRequestParams();
        reqRequestExternalPayment = RequestExternalPayment.Request.newInstance(
                respInstanceId.getInstanceId(), PATTERN_ID_PHONE_TOPUP, params);

        respRequestExternalPayment = ym.execute(reqRequestExternalPayment);

        String successUri = "https://elbrus.yandex.ru/success";
        String failUri = "https://elbrus.yandex.ru/fail";
        reqProcessExternalPayment = new ProcessExternalPayment.Request(
                respInstanceId.getInstanceId(), respRequestExternalPayment.getRequestId(),
                successUri, failUri, false);
        respProcessExternalPayment = ym.execute(reqProcessExternalPayment);

        Assert.assertNotNull(respProcessExternalPayment);
        Assert.assertEquals(respProcessExternalPayment.getStatus(),
                ProcessExternalPayment.Status.EXT_AUTH_REQUIRED);
    }
}
