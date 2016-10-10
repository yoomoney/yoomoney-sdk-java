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

import com.yandex.money.api.exceptions.InsufficientScopeException;
import com.yandex.money.api.exceptions.InvalidRequestException;
import com.yandex.money.api.exceptions.InvalidTokenException;
import com.yandex.money.api.methods.AccountInfo;
import com.yandex.money.api.methods.AuxToken;
import com.yandex.money.api.model.Scope;
import com.yandex.money.api.net.v1.DefaultApiClient;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class TokenTest implements ApiTest {

    @Test
    public void testAuxToken() throws InvalidTokenException, InsufficientScopeException,
            InvalidRequestException, IOException {

        DefaultApiClient client = DEFAULT_API_CLIENT_BUILDER.create();
        client.setAccessToken(ACCESS_TOKEN);

        Set<Scope> scopes = new HashSet<>();
        scopes.add(Scope.ACCOUNT_INFO);

        AuxToken auxToken = client.execute(new AuxToken.Request(scopes));
        String token = auxToken.auxToken;
        Assert.assertNotNull(token);
        client.setAccessToken(token);

        AccountInfo accountInfo = client.execute(new AccountInfo.Request());
        Assert.assertNotNull(accountInfo.account);
        Assert.assertNotNull(accountInfo.balance);
    }
}
