/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 NBCO YooMoney LLC
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

package com.yoo.money.api.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.yoo.money.api.util.Common.checkNotEmpty;
import static com.yoo.money.api.util.Common.checkNotNull;

/**
 * Helps to deal with urls.
 *
 * @author Slava Yasevich (support@yoomoney.ru)
 */
public final class UrlEncodedUtils {

    private UrlEncodedUtils() {
        // prevents instantiating of this class
    }

    /**
     * Parses url to key-value pairs of its parameters.
     *
     * @param url url
     * @return key-value pairs
     */
    public static Map<String, String> parse(String url) throws URISyntaxException {
        URI uri = new URI(checkNotEmpty(url, "url"));
        String query = uri.getQuery();
        if (query == null) {
            return Collections.emptyMap();
        }
        return parseQuery(query);
    }

    /**
     * Parses query component of URI into parameters map.
     *
     * @param query query component of URI
     * @return parameters as key-value pairs
     */
    public static Map<String, String> parseQuery(String query) {
        String[] params = checkNotNull(query, "query").split("&");
        Map<String, String> map = new HashMap<>(params.length);
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                map.put(keyValue[0], keyValue[1]);
            }
        }
        return Collections.unmodifiableMap(map);
    }
}
