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

package com.yandex.money.api.net.clients;

import com.squareup.okhttp.OkHttpClient;
import com.yandex.money.api.net.ApiRequest;
import com.yandex.money.api.net.UserAgent;
import com.yandex.money.api.net.providers.HostsProvider;
import com.yandex.money.api.utils.Language;

/**
 * Yandex.Money API client. Provides necessary information for sessions.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public interface ApiClient {

    /**
     * @return client id
     */
    String getClientId();

    /**
     * @return HTTP client to use when executing API requests
     */
    OkHttpClient getHttpClient();

    /**
     * @return hosts provider
     */
    HostsProvider getHostsProvider();

    /**
     * @return specific HTTP user agent
     */
    UserAgent getUserAgent();

    /**
     * @return language of API responses
     */
    Language getLanguage();

    /**
     * Prepares {@link ApiRequest} to meet {@link ApiClient} requirements.
     *
     * @param request base request
     * @param <T> type of response
     * @return modified request
     */
    <T> ApiRequest<T> prepare(ApiRequest<T> request);

    /**
     * Checks if debug mode is enabled for this client.
     *
     * @return {@code true} if debug mode is enabled
     */
    boolean isDebugEnabled();
}
