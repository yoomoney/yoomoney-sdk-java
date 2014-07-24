package com.yandex.money.test;

import com.yandex.money.exceptions.InsufficientScopeException;
import com.yandex.money.exceptions.InvalidRequestException;
import com.yandex.money.exceptions.InvalidTokenException;
import com.yandex.money.model.methods.Token;
import com.yandex.money.net.DefaultApiClient;
import com.yandex.money.net.OAuth2Session;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class TokenTest implements ApiTest {

    private static final String CODE = "authorization_code";

    @Test
    public void testToken() throws InvalidTokenException, InsufficientScopeException,
            InvalidRequestException, IOException {

        OAuth2Session session = new OAuth2Session(new DefaultApiClient());
        session.setDebugLogging(true);

        Token token = session.execute(new Token.Request(CODE, CLIENT_ID, REDIRECT_URI, null));
        Assert.assertNotNull(token);

        session.setAccessToken(token.getAccessToken());
        session.execute(new Token.Revoke());
    }
}
