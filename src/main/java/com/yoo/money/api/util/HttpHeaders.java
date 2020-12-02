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

import com.yoo.money.api.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * This is not complete list of headers.
 *
 * @author Slava Yasevich (support@yoomoney.ru)
 */
public final class HttpHeaders {

    public static final String ACCEPT_LANGUAGE = "Accept-Language";
    public static final String AUTHORIZATION = "Authorization";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String EXPIRES = "Expires";
    public static final String IF_MODIFIED_SINCE = "If-Modified-Since";
    public static final String LAST_MODIFIED = "Last-Modified";
    public static final String LOCATION = "Location";
    public static final String USER_AGENT = "User-Agent";
    public static final String WWW_AUTHENTICATE = "WWW-Authenticate";

    private static final DateFormat DATE_TIME_FORMATTER =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);

    private HttpHeaders() {
        // prevents instantiating of this class
    }

    public static DateTime parseDateTime(String value) throws ParseException {
        return DateTime.from(DATE_TIME_FORMATTER.parse(value));
    }

    public static String formatDateTime(DateTime value) {
        DATE_TIME_FORMATTER.setTimeZone(value.getTimeZone());
        return DATE_TIME_FORMATTER.format(value.getDate());
    }
}
