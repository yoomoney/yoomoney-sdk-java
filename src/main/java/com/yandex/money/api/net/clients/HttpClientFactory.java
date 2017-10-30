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

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import java.security.GeneralSecurityException;
import java.util.concurrent.TimeUnit;

import static com.yandex.money.api.util.Common.checkNotNull;

/**
 * Creates HTTP clients.
 */
public final class HttpClientFactory {

    private HttpClientFactory() {
    }

    /**
     * Creates new {@link OkHttpClient} instance.
     *
     * @param enableLogging {@code true} if logging is required
     * @return new HTTP client
     */
    public static OkHttpClient newOkHttpClient(boolean enableLogging) {
        OkHttpClient.Builder builder = createDefaultOkHttpClientBuilder();
        if (enableLogging) {
            applyLogging(builder);
        }
        return builder.build();
    }

    /**
     * Creates {@link OkHttpClient.Builder} initialized with default parameters.
     *
     * @return instance of {@link OkHttpClient.Builder}
     */
    public static OkHttpClient.Builder createDefaultOkHttpClientBuilder() {
        final long timeout = 30;
        return new OkHttpClient.Builder()
                .readTimeout(timeout, TimeUnit.SECONDS)
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .connectionPool(new ConnectionPool(4, 10L, TimeUnit.MINUTES))
                .followSslRedirects(false)
                .followRedirects(false);
    }

    /**
     * Applies logging.
     *
     * @param builder builder that will be used to create HTTP client
     */
    public static void applyLogging(OkHttpClient.Builder builder) {
        applyLogging(builder, createSslSocketFactory());
    }

    /**
     * Applies logging to OkHttp client.
     *
     * @param builder OkHttp client builder
     * @param sslSocketFactory SSL socket factory
     */
    public static void applyLogging(OkHttpClient.Builder builder, SSLSocketFactory sslSocketFactory) {
        checkNotNull(builder, "builder").sslSocketFactory(new WireLoggingSocketFactory(sslSocketFactory));
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
