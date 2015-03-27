package com.yandex.money.test;

import com.yandex.money.api.exceptions.InsufficientScopeException;
import com.yandex.money.api.exceptions.InvalidRequestException;
import com.yandex.money.api.exceptions.InvalidTokenException;
import com.yandex.money.api.methods.AccountInfo;
import com.yandex.money.api.net.DefaultApiClient;
import com.yandex.money.api.net.OAuth2Session;

import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class AccountInfoTest implements ApiTest {

    private OAuth2Session session;

    @BeforeTest
    public void beforeTest() {
        session = new OAuth2Session(new DefaultApiClient(CLIENT_ID));
        session.setDebugLogging(true);
        session.setAccessToken(ACCESS_TOKEN);
    }

    @Test
    public void testAccountInfo() throws InvalidTokenException, InsufficientScopeException,
            InvalidRequestException, IOException {

        AccountInfo.Request request = new AccountInfo.Request();
        AccountInfo accountInfo = session.execute(request);
        Assert.assertNotNull(accountInfo);
    }
}
