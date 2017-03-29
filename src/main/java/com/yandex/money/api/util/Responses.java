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

package com.yandex.money.api.util;

import com.yandex.money.api.net.HttpClientResponse;
import com.yandex.money.api.time.DateTime;
import com.yandex.money.api.time.Iso8061Format;

import java.io.IOException;
import java.text.ParseException;

/**
 * @author Slava Yasevich
 */
public final class Responses {

    private Responses() {
    }

    public static DateTime parseDateHeader(HttpClientResponse response, String header) throws ParseException {
        String dateHeader = response.getHeader(header);
        return dateHeader == null || dateHeader.isEmpty() ? DateTime.now() : Iso8061Format.parse(dateHeader);
    }

    /**
     * Processes connection errors.
     *
     * @param response response reference
     * @return WWW-Authenticate field of connection
     */
    public static String processError(HttpClientResponse response) throws IOException {
        String field = response.getHeader(HttpHeaders.WWW_AUTHENTICATE);
        com.yandex.money.api.util.logging.Log.w("Server has responded with a error: " + getError(response) + "\n" +
                HttpHeaders.WWW_AUTHENTICATE + ": " + field);
        com.yandex.money.api.util.logging.Log.w(response.getBody());
        return field;
    }

    private static String getError(HttpClientResponse response) {
        return "HTTP " + response.getCode() + " " + response.getMessage();
    }
}
