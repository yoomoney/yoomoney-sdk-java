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

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.yandex.money.api.utils.HttpHeaders;
import com.yandex.money.api.utils.MimeTypes;
import com.yandex.money.api.utils.Streams;
import com.yandex.money.api.utils.Strings;

import org.joda.time.DateTime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @author Roman Tsirulnikov (romanvt@yamoney.ru)
 */
public class PostRequestBodyBuffer extends ByteArrayOutputStream {

    private static final Charset REQUEST_CHARSET_UTF8 = Charset.forName("UTF-8");

    private static final byte[] POST_PARAM_DELIMITER = "&".getBytes(REQUEST_CHARSET_UTF8);
    private static final byte[] POST_PARAM_NV_DELIMITER = "=".getBytes(REQUEST_CHARSET_UTF8);

    private String contentType = MimeTypes.Application.X_WWW_FORM_URLENCODED;

    /**
     * Adds not null int parameter to buffer.
     *
     * @param name name of parameter
     * @param value its value
     */
    public PostRequestBodyBuffer addParam(String name, int value) {
        return addParam(name, Integer.toString(value));
    }

    /**
     * Adds not null String parameter to buffer.
     *
     * @param name name of parameter
     * @param value its value
     */
    public PostRequestBodyBuffer addParam(String name, String value) {
        if (value == null) {
            value = "";
        }
        try {
            return addParamWithoutURLEncoding(
                    URLEncoder.encode(name, REQUEST_CHARSET_UTF8.name())
                            .getBytes(REQUEST_CHARSET_UTF8),
                    URLEncoder.encode(value, REQUEST_CHARSET_UTF8.name())
                            .replace("+", "%20")
                            .getBytes(REQUEST_CHARSET_UTF8));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * Adds map of name-value pairs to buffer.
     *
     * @param params name-value pairs
     */
    public PostRequestBodyBuffer addParams(Map<String, String> params) {
        if (params == null) {
            throw new NullPointerException("params is null");
        }

        for (Map.Entry<String, String> entry : params.entrySet()) {
            addParam(entry.getKey(), entry.getValue());
        }
        return this;
    }

    /**
     * Adds nullable Integer parameter to buffer.
     *
     * @param name name of parameter
     * @param value its value
     */
    public PostRequestBodyBuffer addParamIfNotNull(String name, Integer value) {
        return value != null ? addParam(name, value.toString()) : this;
    }

    /**
     * Adds nullable Boolean parameter to buffer.
     *
     * @param name name of parameter
     * @param value its value
     */
    public PostRequestBodyBuffer addParamIfNotNull(String name, Boolean value) {
        return value != null ? addParam(name, value.toString()) : this;
    }

    /**
     * Adds nullable String parameter to buffer.
     *
     * @param name name of parameter
     * @param value its value
     */
    public PostRequestBodyBuffer addParamIfNotNull(String name, String value) {
        return value != null ? addParam(name, value) : this;
    }

    /**
     * Adds nullable BigDecimal parameter to buffer.
     *
     * @param name name of parameter
     * @param value its value
     */
    public PostRequestBodyBuffer addParamIfNotNull(String name, BigDecimal value) {
        return value != null ? addParam(name, value.toPlainString()) : this;
    }

    /**
     * Adds nullable DateTime parameter to buffer.
     *
     * @param name name of parameter
     * @param value its value
     */
    public PostRequestBodyBuffer addDateTimeIfNotNull(String name, DateTime value) {
        if (value != null) {
            return addParam(name, value.toString());
        }
        else return this;
    }

    /**
     * Adds Boolean parameter if {@code value == true}.
     *
     * @param name name of parameter
     * @param value its value
     */
    public PostRequestBodyBuffer addBooleanIfTrue(String name, Boolean value) {
        if (value != null && value)
            return addParam(name, "true");
        else
            return this;
    }

    /**
     * Adds bytes to buffer without encoding.
     *
     * @param name of parameter
     * @param value its value
     */
    public PostRequestBodyBuffer addParamWithoutURLEncoding(byte[] name, byte[] value) {
        try {
            if (size() > 0) {
                write(POST_PARAM_DELIMITER);
            }
            write(name);
            write(POST_PARAM_NV_DELIMITER);
            write(value);
            return this;
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    /**
     * Adds content of input stream to buffer
     * @param inputStream source stream
     */
    public PostRequestBodyBuffer addContent(InputStream inputStream) {
        try {
            Streams.copy(inputStream, this);
            return this;
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    /**
     * Sets specific content type if required. By default {@code application/x-www-form-urlencoded}
     * is used.
     *
     * @param contentType content type
     */
    public PostRequestBodyBuffer setContentType(String contentType) {
        if (Strings.isNullOrEmpty(contentType)) {
            throw new IllegalArgumentException("contentType should not be null or empty");
        }
        this.contentType = contentType;
        return this;
    }

    /**
     * Sets HTTP headers for connection.
     *
     * @param connection connection
     */
    @Deprecated
    public void setHttpHeaders(HttpURLConnection connection) throws ProtocolException {
        connection.setRequestMethod("POST");
        connection.setRequestProperty(HttpHeaders.CONTENT_TYPE, contentType);
        connection.setFixedLengthStreamingMode(size());
    }

    /**
     * Writes buffer to outputStream
     *
     * @param out target stream
     */
    @Deprecated
    public void write(OutputStream out) throws IOException {
        out.write(buf, 0, count);
    }

    public RequestBody getRequestBody() {
        return RequestBody.create(MediaType.parse(contentType), buf, 0, count);
    }
}
