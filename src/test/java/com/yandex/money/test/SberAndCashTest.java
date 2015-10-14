package com.yandex.money.test;

import com.yandex.money.api.exceptions.InsufficientScopeException;
import com.yandex.money.api.exceptions.InvalidRequestException;
import com.yandex.money.api.exceptions.InvalidTokenException;
import com.yandex.money.api.methods.*;
import com.yandex.money.api.net.DefaultApiClient;
import com.yandex.money.api.net.OAuth2Session;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitriy Tsirkunov (tsyrkunov@yamoney.ru)
 */

public class SberAndCashTest implements ApiTest{

    private OAuth2Session session;
    private Map<String, String> paramsForRequestPayment = new HashMap<>();
    private final String phoneNumber = LOCAL_PROPERTIES.getPhoneNumber();
    private final BigDecimal amount = LOCAL_PROPERTIES.getAmount();

    @BeforeTest
    public void beforeTest() {
        session = new OAuth2Session(new DefaultApiClient(CLIENT_ID));
        session.setDebugLogging(true);
        paramsForRequestPayment.put("sum", amount.toPlainString());
    }

    @Test
    public void testSberInvoicing() throws InvalidTokenException, InsufficientScopeException, InvalidRequestException, InterruptedException, IOException {
        makeEprPayment("sberbank", "6943");
    }

    @Test
    public void testCash() throws InvalidTokenException, InsufficientScopeException, InvalidRequestException, InterruptedException, IOException {
        makeEprPayment("cash", "6943");
    }


    private void makeEprPayment(String moneySource, String pattern) throws InvalidTokenException, InsufficientScopeException,
            InvalidRequestException, IOException, InterruptedException {
        InstanceId.Request requestInstanceId = new InstanceId.Request(CLIENT_ID);
        InstanceId responseInstanceId = session.execute(requestInstanceId);

        RequestExternalPayment.Request requestExternalPayment = RequestExternalPayment.Request.newInstance(responseInstanceId.instanceId,
                pattern, paramsForRequestPayment);
        BaseRequestPayment response = session.execute(requestExternalPayment);
        Assert.assertNotNull(response.requestId);
        ProcessExternalPayment.Request processPayment =
                new ProcessExternalPayment.Request(responseInstanceId.instanceId,
                        response.requestId,
                        "http://success.ru",
                        "http://fail.ru",
                        moneySource,
                        phoneNumber);
        ProcessExternalPayment ppResponse = session.execute(processPayment);
        while (ppResponse.nextRetry != null) {
            Thread.sleep(ppResponse.nextRetry);
            ppResponse = session.execute(processPayment);
        }
        Assert.assertEquals(ppResponse.status == BaseProcessPayment.Status.SUCCESS, true);

        if (moneySource.equals("cash"))
            System.out.println("paymentCode = " + ppResponse.paymentCode);
        else
            System.out.println("invoiceId = " + ppResponse.invoiceId);

    }
}
