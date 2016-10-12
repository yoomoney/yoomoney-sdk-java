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

import com.yandex.money.api.authorization.AuthorizationData;
import com.yandex.money.api.authorization.AuthorizationParameters;
import com.yandex.money.api.net.ApiRequest;
import com.yandex.money.api.net.DefaultUserAgent;
import com.yandex.money.api.net.UserAgent;
import com.yandex.money.api.net.providers.DefaultApiV1HostsProvider;
import com.yandex.money.api.net.providers.HostsProvider;
import com.yandex.money.api.util.HttpHeaders;
import com.yandex.money.api.util.Language;
import com.yandex.money.api.util.Strings;
import okhttp3.CacheControl;
import okhttp3.ConnectionPool;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.security.GeneralSecurityException;
import java.util.Map;
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

    private final CacheControl cacheControl = new CacheControl.Builder().noCache().build();

    private final String clientId;
    private final HostsProvider hostsProvider;
    private final UserAgent userAgent;
    private final Language language;
    private final boolean debugMode;
    private final OkHttpClient httpClient;

    private String accessToken;

    protected DefaultApiClient(Builder builder) {
        clientId = checkNotNull(builder.clientId, "clientId");
        hostsProvider = builder.hostsProvider;
        userAgent = new DefaultUserAgent(checkNotNull(builder, "builder").platform);
        language = Language.getDefault();
        debugMode = builder.debugMode;

        OkHttpClient.Builder httpClientBuilder = createHttpClientBuilder();
        if (debugMode) {
            SSLSocketFactory sslSocketFactory = createSslSocketFactory();
            httpClientBuilder.sslSocketFactory(new WireLoggingSocketFactory(sslSocketFactory));
        }
        configHttpClient(httpClientBuilder);
        httpClient = httpClientBuilder.build();
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public Language getLanguage() {
        return language;
    }

    @Override
    public HostsProvider getHostsProvider() {
        return hostsProvider;
    }

    @Override
    public <T> T execute(ApiRequest<T> request) throws Exception {
        Response response = httpClient.newCall(prepareRequest(request)).execute();
        return request.parse(new OkHttpClientResponse(response, debugMode));
    }

    @Override
    public AuthorizationData createAuthorizationData(AuthorizationParameters parameters) {
        parameters.add("client_id", getClientId());
        return new AuthorizationDataImpl(getHostsProvider().getWebUrl(), parameters.build());
    }

    @Override
    public final void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public final boolean isAuthorized() {
        return !Strings.isNullOrEmpty(accessToken);
    }

    protected void configHttpClient(OkHttpClient.Builder builder) {
    }

    /**
     * Creates HTTP client to use.
     *
     * @return HTTP client
     */
    private OkHttpClient.Builder createHttpClientBuilder() {
        return new OkHttpClient.Builder()
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(4, 10L, TimeUnit.MINUTES))
                .followSslRedirects(false)
                .followRedirects(false);
    }

    private Request prepareRequest(ApiRequest<?> request) {
        checkNotNull(request, "request");

        Request.Builder builder = new Request.Builder()
                .cacheControl(cacheControl)
                .url(request.requestUrl(getHostsProvider()))
                .addHeader(HttpHeaders.USER_AGENT, userAgent.getName())
                .addHeader(HttpHeaders.ACCEPT_LANGUAGE, getLanguage().iso6391Code);

        if (isAuthorized()) {
            builder.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        }

        for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
            String value = entry.getValue();
            if (value != null) {
                builder.addHeader(entry.getKey(), value);
            }
        }

        RequestBody body = RequestBody.create(MediaType.parse(request.getContentType()), request.getBody());
        switch (request.getMethod()) {
            case POST:
                builder.post(body);
                break;
            case PUT:
                builder.put(body);
                break;
        }

        return builder.build();
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

    private static final class AuthorizationDataImpl implements AuthorizationData {

        private final String url;
        private final byte[] parameters;

        private AuthorizationDataImpl(String host, byte[] parameters) {
            this.url = host + "/oauth/authorize";
            this.parameters = parameters;
        }

        @Override
        public String getUrl() {
            return url;
        }

        @Override
        public byte[] getParameters() {
            return parameters;
        }
    }
}
