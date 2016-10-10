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

package com.yandex.money.api.net;

import com.yandex.money.api.authorization.AuthorizationData;
import com.yandex.money.api.authorization.AuthorizationParameters;
import com.yandex.money.api.net.providers.HostsProvider;

/**
 * Yandex.Money API client.
 */
public interface ApiClient {

    /**
     * Returns client id.
     *
     * @return client id
     */
    String getClientId();

    /**
     * Returns instance of {@link HostsProvider}
     *
     * @return host's provider
     */
    HostsProvider getHostsProvider();

    <T> T execute(ApiRequest<T> request) throws Exception;

    /**
     * Creates {@link AuthorizationData} based on a client's configuration and provided {@link AuthorizationParameters}.
     *
     * @param parameters parameters to use
     * @return authorization data
     */
    AuthorizationData createAuthorizationData(AuthorizationParameters parameters);

    /**
     * Checks if client is in authorized state.
     *
     * @return {@code true}, if client is authorized
     */
    boolean isAuthorized();

    /**
     * Checks if debug mode is enabled for this client.
     *
     * @return {@code true} if debug mode is enabled
     */
    boolean isDebugEnabled();
}
