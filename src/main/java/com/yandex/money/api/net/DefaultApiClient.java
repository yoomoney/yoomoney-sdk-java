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

package com.yandex.money.api.net;

import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.OkHttpClient;
import com.yandex.money.api.utils.Language;
import com.yandex.money.api.utils.Strings;

import java.security.GeneralSecurityException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import static com.yandex.money.api.utils.Common.checkNotNull;

/**
 * Default implementation of {@link com.yandex.money.api.net.ApiClient} interface.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class DefaultApiClient implements ApiClient {

    private static final long DEFAULT_TIMEOUT = 30;

    private final String id;
    private final OkHttpClient httpClient;
    private final HostsProvider hostsProvider;

    private String platform = "Java";

    /**
     * Constructor.
     *
     * @param clientId client id to use
     */
    public DefaultApiClient(String clientId) {
        checkNotNull(clientId, "client id");
        id = clientId;
        httpClient = createHttpClient();
        hostsProvider = new HostsProvider(false);
    }

    /**
     * Constructor.
     *
     * @param clientId client id to use
     * @param debugLogging {@code true} if logging is required
     */
    public DefaultApiClient(String clientId, boolean debugLogging) {
        this(clientId);
        if (debugLogging) {
            SSLSocketFactory sslSocketFactory = createSslSocketFactory();
            httpClient.setSslSocketFactory(new WireLoggingSocketFactory(sslSocketFactory));
        }
    }

    /**
     * Constructor.
     * <p/>
     * If {@code platform} parameter is null or empty default value will be used. No exception will
     * be thrown.
     *
     * @param clientId client id to use
     * @param debugLogging {@code true} if logging is required
     * @param platform the name of a platform client is running on
     */
    public DefaultApiClient(String clientId, boolean debugLogging, String platform) {
        this(clientId, debugLogging);
        if (!Strings.isNullOrEmpty(platform)) {
            this.platform = platform;
        }
    }

    @Override
    public String getClientId() {
        return id;
    }

    @Override
    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    @Override
    public HostsProvider getHostsProvider() {
        return hostsProvider;
    }

    @Override
    public UserAgent getUserAgent() {
        return new DefaultUserAgent(platform);
    }

    @Override
    public Language getLanguage() {
        return Language.getDefault();
    }

    private static OkHttpClient createHttpClient() {
        OkHttpClient client = new OkHttpClient();
        client.setReadTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        client.setConnectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        client.setConnectionPool(new ConnectionPool(4, 10 * 60 * 1000L));
        client.setFollowSslRedirects(false);
        client.setFollowRedirects(false);
        return client;
    }

    private static SSLSocketFactory createSslSocketFactory() {
        try {
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, null, null);
            return context.getSocketFactory();
        } catch (GeneralSecurityException exception) {
            throw new RuntimeException(exception);
        }
    }
}
