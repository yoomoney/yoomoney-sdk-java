/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 NBCO YooMoney LLC
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

package com.yoo.money.api.authorization;

import com.yoo.money.api.model.Scope;
import com.yoo.money.api.util.UrlEncodedUtils;
import org.testng.annotations.Test;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public final class AuthorizationParametersTest {

    @Test
    public void testCreation() throws URISyntaxException, UnsupportedEncodingException {
        final String instanceName = "INSTANCE_NAME";
        final String redirectUri = "https://yoomoney.ru";

        final List<Scope> scopes = Arrays.asList(Scope.ACCOUNT_INFO, Scope.OPERATION_HISTORY, Scope.OPERATION_DETAILS);
        final List<String> expectedScopes = new ArrayList<>(scopes.size());
        for (Scope scope : scopes) {
            expectedScopes.add(scope.getQualifiedName());
        }

        AuthorizationParameters.Builder builder = new AuthorizationParameters.Builder();
        for (Scope scope : scopes) {
            builder.addScope(scope);
        }

        AuthorizationParameters parameters = builder.setInstanceName(instanceName)
                .setRedirectUri(redirectUri)
                .create();

        assertNotNull(parameters);

        byte[] bytes = parameters.build();
        assertNotNull(bytes);

        Map<String, String> map = UrlEncodedUtils.parseQuery(new String(bytes, "UTF-8"));
        assertNotNull(map);

        assertEquals(decode(map.get("instance_name")), instanceName);
        assertEquals(decode(map.get("response_type")), "code");
        assertEquals(decode(map.get("redirect_uri")), redirectUri);

        String scope = decode(map.get("scope"));
        assertNotNull(scope);

        String[] split = scope.split(" ");
        assertEquals(split.length, scopes.size());

        for (String actual : split) {
            assertTrue(expectedScopes.contains(actual));
        }
    }

    private static String decode(String parameter) throws UnsupportedEncodingException {
        return URLDecoder.decode(parameter, "UTF-8");
    }
}
