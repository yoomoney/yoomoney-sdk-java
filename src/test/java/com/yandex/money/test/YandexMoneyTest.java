package com.yandex.money.test;

import com.yandex.money.YandexMoney;
import com.yandex.money.exceptions.InsufficientScopeException;
import com.yandex.money.exceptions.InvalidRequestException;
import com.yandex.money.exceptions.InvalidTokenException;
import com.yandex.money.model.common.params.PhoneParams;
import com.yandex.money.model.cps.Error;
import com.yandex.money.model.cps.InstanceId;
import com.yandex.money.model.cps.ProcessExternalPayment;
import com.yandex.money.model.cps.RequestExternalPayment;

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

    private String PATTERN_ID_PHONE_TOPUP = PhoneParams.PATTERN_ID;

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

        respRequestExternalPayment = ym.execute(reqRequestExternalPayment);

        Assert.assertNotNull(respRequestExternalPayment);
        Assert.assertEquals(respRequestExternalPayment.getStatus(),
                RequestExternalPayment.Status.SUCCESS);
        Assert.assertNull(respRequestExternalPayment.getError());
        Assert.assertNotNull(respRequestExternalPayment.getRequestId());
        Assert.assertTrue(respRequestExternalPayment.getRequestId().length() > 0);
        Assert.assertEquals(respRequestExternalPayment.getContractAmount(),
                new BigDecimal("23.00"));
    }

    private HashMap<String, String> successRequestParams() {
        HashMap<String, String> params = new HashMap<>();
        params.put("amount", "23");
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

        respRequestExternalPayment = ym.execute(reqRequestExternalPayment);

        Assert.assertNotNull(respRequestExternalPayment);
        Assert.assertEquals(respRequestExternalPayment.getStatus(),
                RequestExternalPayment.Status.REFUSED);
        Assert.assertEquals(respRequestExternalPayment.getError(), Error.ILLEGAL_PARAMS);
        Assert.assertNull(respRequestExternalPayment.getRequestId());

        params = successRequestParams();
        params.remove("amount");
        reqRequestExternalPayment = RequestExternalPayment.Request.newInstance(
                respInstanceId.getInstanceId(), PATTERN_ID_PHONE_TOPUP, params);
        respRequestExternalPayment = ym.execute(reqRequestExternalPayment);

        params = successRequestParams();
        params.remove("phone-number");
        reqRequestExternalPayment = RequestExternalPayment.Request.newInstance(
                respInstanceId.getInstanceId(), PATTERN_ID_PHONE_TOPUP, params);
        respRequestExternalPayment = ym.execute(reqRequestExternalPayment);
        Assert.assertNotNull(respRequestExternalPayment);
        Assert.assertEquals(respRequestExternalPayment.getStatus(),
                RequestExternalPayment.Status.REFUSED);
        Assert.assertEquals(respRequestExternalPayment.getError(), Error.ILLEGAL_PARAMS);
        Assert.assertNull(respRequestExternalPayment.getRequestId());
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
