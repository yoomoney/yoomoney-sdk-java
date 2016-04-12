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

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * API requests implement this interface. Consider to use {@link BaseApiRequest} as your base class.
 *
 * @param <T> response
 * @param <E> request params type
 * @see BaseApiRequest
 */
public abstract class ApiRequest<T, E> {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat
            .forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT")
            .withLocale(Locale.US)
            .withZoneUTC();

    private final Map<String, String> headers = new HashMap<>();

    /**
     * Adds {@link String} header to this request.
     *
     * @param key key
     * @param value value
     */
    protected final void addHeader(String key, String value) {
        headers.put(key, value);
    }

    /**
     * Gets headers for a request. Can not be null.
     *
     * @return headers for a request
     */
    public final Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    /**
     * Adds collection of headers.
     *
     * @param headers headers to add
     */
    protected final void addHeaders(Map<String, String> headers) {
        this.headers.putAll(headers);
    }

    /**
     * Adds {@link DateTime} header to this request.
     *
     * @param key key
     * @param value value
     */
    protected final void addHeader(String key, DateTime value) {
        addHeader(key, value == null ? null : DATE_TIME_FORMATTER.print(value));
    }

    /**
     * Gets method for a request.
     *
     * @return method
     */
    public abstract Method getMethod();

    /**
     * Builds URL with using specified hosts provider.
     *
     * @param hostsProvider hosts provider
     * @return complete URL
     */
    public abstract String requestUrl(HostsProvider hostsProvider);

    /**
     * Gets post parameters to use when posting a request. Can not be null.
     *
     * @return parameters for a request
     */
    public abstract Map<String, E> getParameters();

    /**
     * Parses API response from stream.
     *
     * @param inputStream input stream
     * @return response
     */
    public abstract T parseResponse(InputStream inputStream);

    /**
     * Methods enum.
     */
    public enum Method {
        GET,
        POST
    }
}
