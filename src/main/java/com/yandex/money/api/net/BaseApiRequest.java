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

import com.yandex.money.api.typeadapters.TypeAdapter;
import org.joda.time.DateTime;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.yandex.money.api.utils.Common.checkNotNull;

/**
 * Base API request. It is preferable to extend your requests from this class or its descendants
 * rather than {@link ApiRequest}.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public abstract class BaseApiRequest<T> extends ApiRequest<T, String> {

    private final TypeAdapter<T> typeAdapter;
    private final Map<String, String> parameters = new HashMap<>();

    /**
     * Constructor.
     *
     * @param typeAdapter typeAdapter used to parse a response
     */
    protected BaseApiRequest(TypeAdapter<T> typeAdapter) {
        this.typeAdapter = checkNotNull(typeAdapter, "typeAdapter");
    }

    @Override
    public final Map<String, String> getParameters() {
        return Collections.unmodifiableMap(parameters);
    }

    @Override
    public final T parseResponse(InputStream inputStream) {
        return typeAdapter.fromJson(inputStream);
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
     * Adds {@link Long} parameter to this request.
     *
     * @param key key
     * @param value value
     */
    protected final void addParameter(String key, Long value) {
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
