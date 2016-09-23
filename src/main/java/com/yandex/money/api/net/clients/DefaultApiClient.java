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

import com.yandex.money.api.net.ApiRequest;
import com.yandex.money.api.net.DefaultUserAgent;
import com.yandex.money.api.net.UserAgent;
import com.yandex.money.api.net.providers.DefaultApiV1HostsProvider;
import com.yandex.money.api.net.providers.HostsProvider;
import com.yandex.money.api.util.Language;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.security.GeneralSecurityException;
import java.util.concurrent.TimeUnit;

import static com.yandex.money.api.util.Common.checkNotEmpty;
import static com.yandex.money.api.util.Common.checkNotNull;

/**
 * Default implementation of {@link ApiClient} interface.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class DefaultApiClient implements ApiClient {

    private static final long DEFAULT_TIMEOUT = 30;

    private final String clientId;
    private final OkHttpClient httpClient;
    private final HostsProvider hostsProvider;
    private final Language language;
    private final UserAgent userAgent;
    private final boolean debugMode;

    protected DefaultApiClient(Builder builder) {
        clientId = checkNotNull(builder.clientId, "clientId");
        hostsProvider = builder.hostsProvider;
        userAgent = new DefaultUserAgent(checkNotNull(builder, "builder").platform);
        language = Language.getDefault();
        debugMode = builder.debugMode;

        OkHttpClient.Builder httpClientBuilder = getHttpClientBuilder();
        if (debugMode) {
            SSLSocketFactory sslSocketFactory = createSslSocketFactory();
            httpClientBuilder.sslSocketFactory(new WireLoggingSocketFactory(sslSocketFactory));
        }
        httpClient = httpClientBuilder.build();
    }

    @Override
    public String getClientId() {
        return clientId;
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
        return userAgent;
    }

    @Override
    public Language getLanguage() {
        return language;
    }

    @Override
    public <T> ApiRequest<T> prepare(ApiRequest<T> request) {
        return request;
    }

    @Override
    public boolean isDebugEnabled() {
        return debugMode;
    }

    /**
     * Creates HTTP client to use.
     *
     * @return HTTP client
     */
    protected OkHttpClient.Builder getHttpClientBuilder() {
        return new OkHttpClient.Builder()
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(4, 10L, TimeUnit.MINUTES))
                .followSslRedirects(false)
                .followRedirects(false);
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

    public static class Builder {

        private boolean debugMode = false;
        private String clientId;
        private String platform = "Java";
        private HostsProvider hostsProvider = new DefaultApiV1HostsProvider(false);

        public final Builder setDebugMode(boolean debugMode) {
            this.debugMode = debugMode;
            return this;
        }

        public final Builder setClientId(String clientId) {
            this.clientId = clientId;
            return this;
        }

        public final Builder setPlatform(String platform) {
            this.platform = checkNotEmpty(platform, "platform");
            return this;
        }

        public final Builder setHostsProvider(HostsProvider hostsProvider) {
            this.hostsProvider = checkNotNull(hostsProvider, "hostsProvider");
            return this;
        }

        public DefaultApiClient create() {
            return new DefaultApiClient(this);
        }
    }
}
