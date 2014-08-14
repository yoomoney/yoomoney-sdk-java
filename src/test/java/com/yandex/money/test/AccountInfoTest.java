package com.yandex.money.test;

import com.yandex.money.YandexMoney;
import com.yandex.money.exceptions.InsufficientScopeException;
import com.yandex.money.exceptions.InvalidRequestException;
import com.yandex.money.exceptions.InvalidTokenException;
import com.yandex.money.methods.AccountInfo;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class AccountInfoTest implements ApiTest {

    private YandexMoney yandexMoney;

    @BeforeTest
    public void beforeTest() {
        yandexMoney = new YandexMoney(CLIENT_ID);
        yandexMoney.setDebugLogging(true);
        yandexMoney.setAccessToken(ACCESS_TOKEN);
    }

    @Test
    public void testAccountInfo() throws InvalidTokenException, InsufficientScopeException,
            InvalidRequestException, IOException {

        AccountInfo.Request request = new AccountInfo.Request();
        AccountInfo accountInfo = yandexMoney.execute(request);
        Assert.assertNotNull(accountInfo);
    }
}
