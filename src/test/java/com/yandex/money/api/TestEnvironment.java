/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 NBCO Yandex.Money LLC
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

import com.yandex.money.api.net.clients.ApiClient;
import com.yandex.money.api.net.clients.DefaultApiClient;
import com.yandex.money.api.properties.LocalProperties;

/**
 * @author Slava Yasevich
 */
public final class TestEnvironment {

    private static final LocalProperties LOCAL_PROPERTIES = new LocalProperties();

    private TestEnvironment() {
    }

    public static ApiClient createClient() {
        return new DefaultApiClient.Builder()
                .setClientId(getClientId())
                .setDebugMode(true)
                .create();
    }

    static ApiClient createAuthorizedClient() {
        ApiClient client = createClient();
        client.setAccessToken(getAccessToken());
        return client;
    }

    static String getClientId() {
        return LOCAL_PROPERTIES.getClientId();
    }

    static String getAccessToken() {
        return LOCAL_PROPERTIES.getAccessToken();
    }

    static LocalProperties getLocalProperties() {
        return LOCAL_PROPERTIES;
    }
}