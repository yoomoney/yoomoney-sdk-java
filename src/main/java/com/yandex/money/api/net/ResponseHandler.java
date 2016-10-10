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

package com.yandex.money.api.net;

import com.yandex.money.api.util.HttpHeaders;
import com.yandex.money.api.util.Log;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Slava Yasevich
 */
public final class ResponseHandler {

    private ResponseHandler() {
    }

    /**
     * Gets input stream from response. Logging can be applied if debug parameter is set to {@code true}.
     *
     * @param response response reference
     * @param debug {@code true} if debug is enabled
     * @return input stream
     */
    public static InputStream getInputStream(Response response, boolean debug) throws IOException {
        InputStream stream = response.body().byteStream();
        return debug ? new ResponseLoggingInputStream(stream) : stream;
    }

    /**
     * Processes connection errors.
     *
     * @param response response reference
     * @return WWW-Authenticate field of connection
     */
    public static String processError(Response response) throws IOException {
        String field = response.header(HttpHeaders.WWW_AUTHENTICATE);
        Log.w("Server has responded with a error: " + getError(response) + "\n" +
                HttpHeaders.WWW_AUTHENTICATE + ": " + field);
        Log.w(response.body().string());
        return field;
    }

    private static String getError(Response response) {
        return "HTTP " + response.code() + " " + response.message();
    }
}
