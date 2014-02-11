package com.yandex.money.test;

import com.yandex.money.YandexMoney;
import com.yandex.money.model.InstanceId;
import com.yandex.money.model.ProcessExternalPayment;
import com.yandex.money.model.RequestExternalPayment;
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
public class YandexMoneyTest {

    private String CLIENT_ID = "4292CF5DF43C33F44EA913794223592481551D6FE8B7448013D6AF1F3491E2FE";
    private String PATTERN_ID_PHONE_TOPUP = "phone-topup";

    private YandexMoney ym;

    InstanceId respInstanceId;
    InstanceId.Request reqInstanceId;

    RequestExternalPayment respRequestExternalPayment;
    RequestExternalPayment.Request reqRequestExternalPayment;

    ProcessExternalPayment respProcessExternalPayment;
    ProcessExternalPayment.Request reqProcessExternalPayment;


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
    public void testInstanceIdSuccess() throws IOException {
        reqInstanceId = new InstanceId.Request(CLIENT_ID);
        respInstanceId = ym.performRequest(reqInstanceId);

        Assert.assertEquals(respInstanceId.getStatus(), "success");
        Assert.assertNotNull(respInstanceId.getInstanceId());
        Assert.assertNull(respInstanceId.getError());
    }

    @Test
    public void testInstanceIdFail() throws IOException {
        reqInstanceId = new InstanceId.Request(" ");
        respInstanceId = ym.performRequest(reqInstanceId);

        Assert.assertEquals(respInstanceId.getStatus(), "refused");
        Assert.assertNotNull(respInstanceId.getError());
        Assert.assertEquals(respInstanceId.getError(), "illegal_param_client_id");
        Assert.assertNull(respInstanceId.getInstanceId());
    }

    @Test
    public void testRequestExternalPayment() throws IOException {
        reqInstanceId = new InstanceId.Request(CLIENT_ID);
        respInstanceId = ym.performRequest(reqInstanceId);

        String patternId = "phone-topup";
        HashMap<String, String> params = successRequestParams();
        reqRequestExternalPayment = RequestExternalPayment.Request.newInstance(null, respInstanceId.getInstanceId(), patternId, params);

        respRequestExternalPayment = ym.performRequest(reqRequestExternalPayment);

        Assert.assertNotNull(respRequestExternalPayment);
        Assert.assertEquals(respRequestExternalPayment.getStatus(), "success");
        Assert.assertNull(respRequestExternalPayment.getError());
        Assert.assertNotNull(respRequestExternalPayment.getRequestId());
        Assert.assertTrue(respRequestExternalPayment.getRequestId().length() > 0);
        Assert.assertEquals(respRequestExternalPayment.getContractAmount(), new BigDecimal("23.00"));
        Assert.assertEquals(respRequestExternalPayment.getTitle(), "МТС (Россия)");
    }

    private HashMap<String, String> successRequestParams() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("amount", "23");
        params.put("phone-number", "79112611383");
        return params;
    }

    @Test
    public void testRequestExternalFail() throws IOException {
        reqInstanceId = new InstanceId.Request(CLIENT_ID);
        respInstanceId = ym.performRequest(reqInstanceId);

        HashMap<String, String> params = successRequestParams();
        reqRequestExternalPayment = RequestExternalPayment.Request.newInstance(null, respInstanceId.getInstanceId(), " ", params);

        respRequestExternalPayment = ym.performRequest(reqRequestExternalPayment);

        Assert.assertNotNull(respRequestExternalPayment);
        Assert.assertEquals(respRequestExternalPayment.getStatus(), "refused");
        Assert.assertEquals(respRequestExternalPayment.getError(), "illegal_params");
        Assert.assertNull(respRequestExternalPayment.getRequestId());

        params = successRequestParams();
        params.remove("amount");
        reqRequestExternalPayment = RequestExternalPayment.Request.newInstance(null, respInstanceId.getInstanceId(), PATTERN_ID_PHONE_TOPUP, params);
        respRequestExternalPayment = ym.performRequest(reqRequestExternalPayment);

        params = successRequestParams();
        params.remove("phone-number");
        reqRequestExternalPayment = RequestExternalPayment.Request.newInstance(null, respInstanceId.getInstanceId(), PATTERN_ID_PHONE_TOPUP, params);
        respRequestExternalPayment = ym.performRequest(reqRequestExternalPayment);
        Assert.assertNotNull(respRequestExternalPayment);
        Assert.assertEquals(respRequestExternalPayment.getStatus(), "refused");
        Assert.assertEquals(respRequestExternalPayment.getError(), "illegal_params");
        Assert.assertNull(respRequestExternalPayment.getRequestId());
    }

    @Test
    public void testProcessExternalPayment() throws IOException {
        reqInstanceId = new InstanceId.Request(CLIENT_ID);
        respInstanceId = ym.performRequest(reqInstanceId);

        HashMap<String, String> params = successRequestParams();
        reqRequestExternalPayment = RequestExternalPayment.Request.newInstance(null, respInstanceId.getInstanceId(), PATTERN_ID_PHONE_TOPUP, params);

        respRequestExternalPayment = ym.performRequest(reqRequestExternalPayment);

        String successUri = "https://elbrus.yandex.ru/success";
        String failUri = "https://elbrus.yandex.ru/fail";
        reqProcessExternalPayment = new ProcessExternalPayment.Request(null, respInstanceId.getInstanceId(),
                respRequestExternalPayment.getRequestId(), successUri, failUri, false);
        respProcessExternalPayment = ym.performRequest(reqProcessExternalPayment);

        Assert.assertNotNull(respProcessExternalPayment);
        Assert.assertEquals(respProcessExternalPayment.getStatus(), "ext_auth_required");
    }
}
