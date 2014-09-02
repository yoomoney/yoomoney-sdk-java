package com.yandex.money.test;

import com.yandex.money.api.exceptions.InsufficientScopeException;
import com.yandex.money.api.exceptions.InvalidRequestException;
import com.yandex.money.api.exceptions.InvalidTokenException;
import com.yandex.money.api.methods.AccountInfo;
import com.yandex.money.api.methods.AuxToken;
import com.yandex.money.api.methods.Token;
import com.yandex.money.api.model.Scope;
import com.yandex.money.api.net.DefaultApiClient;
import com.yandex.money.api.net.OAuth2Session;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class TokenTest implements ApiTest {

    private static final String CODE = "authorization_code";

    @Test
    public void testToken() throws InvalidTokenException, InsufficientScopeException,
            InvalidRequestException, IOException {

        OAuth2Session session = new OAuth2Session(new DefaultApiClient(CLIENT_ID, true));
        session.setDebugLogging(true);

        Token token = session.execute(new Token.Request(CODE, CLIENT_ID, REDIRECT_URI, null));
        Assert.assertNotNull(token);

        session.setAccessToken(token.getAccessToken());
        session.execute(new Token.Revoke());
    }

    @Test
    public void testAuxToken() throws InvalidTokenException, InsufficientScopeException,
            InvalidRequestException, IOException {

        OAuth2Session session = new OAuth2Session(new DefaultApiClient(CLIENT_ID, true));
        session.setDebugLogging(true);
        session.setAccessToken(ACCESS_TOKEN);

        Set<Scope> scopes = new HashSet<>();
        scopes.add(Scope.ACCOUNT_INFO);

        AuxToken auxToken = session.execute(new AuxToken.Request(scopes));
        String token = auxToken.getAuxToken();
        Assert.assertNotNull(token);
        session.setAccessToken(token);

        AccountInfo accountInfo = session.execute(new AccountInfo.Request());
        Assert.assertNotNull(accountInfo.getAccount());
        Assert.assertNotNull(accountInfo.getBalance());
    }
}
