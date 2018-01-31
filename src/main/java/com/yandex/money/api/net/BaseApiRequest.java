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

import com.google.gson.JsonElement;
import com.yandex.money.api.net.providers.HostsProvider;
import com.yandex.money.api.time.DateTime;
import com.yandex.money.api.time.Iso8601Format;
import com.yandex.money.api.typeadapters.JsonUtils;
import com.yandex.money.api.util.HttpHeaders;
import com.yandex.money.api.util.MimeTypes;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Base implementation of {@link ApiRequest}. It is preferable to extend your requests from this class or its
 * descendants rather than create your own implementation of {@link ApiRequest}.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public abstract class BaseApiRequest<T> implements ApiRequest<T> {

    private transient final Map<String, String> headers = new HashMap<>();
    private transient final Map<String, String> parameters = new HashMap<>();
    private transient final ParametersBuffer buffer = new ParametersBuffer();

    private transient byte[] body;

    @Override
    public final String requestUrl(HostsProvider hostsProvider) {
        String url = requestUrlBase(hostsProvider);
        return getMethod().supportsRequestBody() ? url : url + buffer.setParameters(parameters).prepareGet();
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
    public final byte[] getBody() {
        prepareBody();
        return body == null ? buffer.setParameters(parameters).prepareBytes() : body;
    }

    @Override
    public String getContentType() {
        return MimeTypes.Application.X_WWW_FORM_URLENCODED;
    }

    /**
     * Creates base URL of a request. For instance base URL for https://money.yandex.ru/api/method?param=value is
     * https://money.yandex.ru/api/method.
     *
     * @param hostsProvider hosts provider
     * @return base URL of a request
     */
    protected abstract String requestUrlBase(HostsProvider hostsProvider);

    /**
     * Adds {@link String} header to this request.
     *
     * @param key key
     * @param value value
     */
    @SuppressWarnings("WeakerAccess")
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
        addHeader(key, value == null ? null : HttpHeaders.formatDateTime(value));
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
     * Adds {@link Double} parameter to this request.
     *
     * @param key key
     * @param value value
     */
    protected final void addParameter(String key, Double value) {
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
        addParameter(key, dateTime == null ? null : Iso8601Format.format(dateTime));
    }

    /**
     * Adds collection of parameters.
     *
     * @param parameters parameters to add
     */
    protected final void addParameters(Map<String, String> parameters) {
        this.parameters.putAll(parameters);
    }

    /**
     * Sets a body for a request. Will override any added parameters if not {code null}.
     *
     * @param body body of a request
     */
    @SuppressWarnings("WeakerAccess")
    protected final void setBody(byte[] body) {
        this.body = body;
    }

    /**
     * Sets a JSON body. Will override any added parameters if not {code null}.
     *
     * @param json JSON body
     * @see #setBody(byte[])
     */
    protected final void setBody(JsonElement json) {
        setBody(JsonUtils.getBytes(json));
    }

    /**
     * Allows you to lazily prepare request body before {@link #getBody()} method returns. You can use
     * {@link #setBody(byte[])} or any of {@code addParameter*} methods here.
     */
    @SuppressWarnings("WeakerAccess")
    protected void prepareBody() {
    }
}
