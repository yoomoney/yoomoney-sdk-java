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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Base API request. It is preferable to extend your requests from this class or its descendants
 * rather than {@link ApiRequest}.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public abstract class BaseApiRequest<T> implements ApiRequest<T> {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat
            .forPattern("EEE, dd MMM yyyy HH:mm:ss 'GMT")
            .withLocale(Locale.US)
            .withZoneUTC();

    private final Class<T> cls;
    private final Gson gson;
    private final Map<String, String> headers = new HashMap<>();
    private final Map<String, String> parameters = new HashMap<>();

    /**
     * Constructor.
     *
     * @param cls class of response
     * @param deserializer deserializer used to create a response
     */
    protected BaseApiRequest(Class<T> cls, JsonDeserializer<T> deserializer) {
        if (cls == null) {
            throw new NullPointerException("response class is null");
        }
        if (deserializer == null) {
            throw new NullPointerException("response deserializer is null");
        }
        this.cls = cls;
        gson = new GsonBuilder()
                .registerTypeAdapter(cls, deserializer)
                .create();
    }

    @Override
    public final Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(headers);
    }

    @Override
    public final Map<String, String> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    @Override
    public final T parseResponse(InputStream inputStream) {
        return gson.fromJson(new InputStreamReader(inputStream), cls);
    }

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
     * Adds {@link DateTime} header to this request.
     *
     * @param key key
     * @param value value
     */
    protected final void addHeader(String key, DateTime value) {
        addHeader(key, DATE_TIME_FORMATTER.print(value));
    }

    /**
     * Adds {@link String} parameter to this request.
     *
     * @param key key
     * @param value value
     */
    protected final void addParameter(String key, String value) {
        parameters.put(key, value);
    }

    /**
     * Adds {@link Integer} parameter to this request.
     *
     * @param key key
     * @param value value
     */
    protected final void addParameter(String key, Integer value) {
        addParameter(key, value == null ? null : value.toString());
    }

    /**
     * Adds {@link Boolean} parameter to this request.
     *
     * @param key key
     * @param value value
     */
    protected final void addParameter(String key, Boolean value) {
        addParameter(key, value == null ? null : value.toString());
    }

    /**
     * Adds {@link BigDecimal} parameter to this request.
     *
     * @param key key
     * @param value value
     */
    protected final void addParameter(String key, BigDecimal value) {
        addParameter(key, value == null ? null : value.toPlainString());
    }

    /**
     * Adds {@link DateTime} parameter to this request.
     *
     * @param key key
     * @param dateTime value
     */
    protected final void addParameter(String key, DateTime dateTime) {
        addParameter(key, dateTime == null ? null : dateTime.toString());
    }

    /**
     * Adds collection of parameters.
     *
     * @param parameters parameters to add
     */
    protected final void addParameters(Map<String, String> parameters) {
        this.parameters.putAll(parameters);
    }
}
