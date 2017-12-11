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

import com.yandex.money.api.util.logging.Log;
import okhttp3.ConnectionPool;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import javax.net.ssl.SSLSocketFactory;
import java.util.concurrent.TimeUnit;

import static com.yandex.money.api.util.Common.checkNotNull;

/**
 * Creates HTTP clients.
 */
public final class HttpClientFactory {

    private static Interceptor loggingInterceptor;

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
    @SuppressWarnings("WeakerAccess")
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
    @SuppressWarnings("WeakerAccess")
    public static void applyLogging(OkHttpClient.Builder builder) {
        checkNotNull(builder, "builder").addInterceptor(getLoggingInterceptor());
    }

    /**
     * Applies logging to OkHttp client.
     *
     * @param builder OkHttp client builder
     * @param sslSocketFactory ignored as of deprecation
     *
     * @deprecated use {@link #applyLogging(OkHttpClient.Builder)} instead
     */
    @Deprecated
    public static void applyLogging(
            OkHttpClient.Builder builder,
            @SuppressWarnings("unused") SSLSocketFactory sslSocketFactory
    ) {
        applyLogging(builder);
    }

    private synchronized static Interceptor getLoggingInterceptor() {
        if (loggingInterceptor == null) {
            loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.i(message);
                }
            }).setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        return loggingInterceptor;
    }
}
