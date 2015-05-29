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
import com.yandex.money.api.utils.MimeTypes;
import com.yandex.money.api.utils.Strings;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;

/**
 * Buffers request parameters and creates request body for different methods. It also encodes keys
 * and values if needed using UTF-8 charset.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public final class ParametersBuffer {

    private static final String UTF8_NAME = "UTF-8";
    private static final Charset UTF8_CHARSET = Charset.forName(UTF8_NAME);
    private static final MediaType CONTENT_TYPE = MediaType.parse(
            MimeTypes.Application.X_WWW_FORM_URLENCODED);

    private Map<String, String> params = Collections.emptyMap();

    /**
     * Sets parameters.
     *
     * @param params key-value pairs of parameters (not null)
     * @return itself
     */
    public ParametersBuffer setParams(Map<String, String> params) {
        if (params == null) {
            throw new NullPointerException("params is null");
        }
        this.params = params;
        return this;
    }

    /**
     * Prepares part of url for get request.
     * <p>
     * For example, there are two parameters provided:
     * <p>
     * {@code Map<String, String> params = new HashMap<>();}<br/>
     * {@code params.put("key1", "value1");}<br/>
     * {@code params.put("key2", "value2");}
     * <p>
     * Then the method will return a string "?key1=value1&key2=value2".
     *
     * @return url parameters
     */
    public String prepareGet() {
        GetBuffer buffer = new GetBuffer();
        iterate(buffer);
        return buffer.toString();
    }

    /**
     * Prepares POST body for a request.
     * <p>
     * For example, there are two parameters provided:
     * <p>
     * {@code Map<String, String> params = new HashMap<>();}
     * {@code params.put("key1", "value1");}<br/>
     * {@code params.put("key2", "value2");}
     * <p>
     * Then the method will return a body "key1=value1&key2=value2".
     *
     * @return body
     */
    public RequestBody preparePost() {
        PostBuffer buffer = new PostBuffer();
        iterate(buffer);
        return RequestBody.create(CONTENT_TYPE, buffer.getBytes());
    }

    private void iterate(Buffer buffer) {
        for (Map.Entry<String, String> param : params.entrySet()) {
            String key = param.getKey();
            if (Strings.isNullOrEmpty(key)) {
                continue; // ignore empty keys
            }

            String value = param.getValue();
            if (value == null) {
                value = "";
            }

            buffer.nextParameter(key, value);
        }
    }

    private static String encode(String value) throws UnsupportedEncodingException {
        return URLEncoder.encode(value, UTF8_NAME);
    }

    private interface Buffer {
        void nextParameter(String key, String value);
    }

    private static final class GetBuffer implements Buffer {

        public final StringBuilder builder = new StringBuilder();

        @Override
        public void nextParameter(String key, String value) {
            try {
                builder.append(builder.length() == 0 ? '?' : '&')
                        .append(encode(key))
                        .append('=')
                        .append(value);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public String toString() {
            return builder.toString();
        }
    }

    private static final class PostBuffer implements Buffer {

        private static final byte[] AMPERSAND = "&".getBytes(UTF8_CHARSET);
        private static final byte[] EQUALS_SIGN = "=".getBytes(UTF8_CHARSET);

        private final ByteArrayOutputStream stream = new ByteArrayOutputStream();

        @Override
        public void nextParameter(String key, String value) {
            try {
                if (stream.size() > 0) {
                    stream.write(AMPERSAND);
                }
                stream.write(encode(key).getBytes(UTF8_CHARSET));
                stream.write(EQUALS_SIGN);
                stream.write(encode(value).replace("+", "%20").getBytes(UTF8_CHARSET));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public byte[] getBytes() {
            return stream.toByteArray();
        }
    }
}
