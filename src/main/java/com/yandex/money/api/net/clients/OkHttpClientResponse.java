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

package com.yandex.money.api.net.clients;

import com.yandex.money.api.net.HttpClientResponse;
import com.yandex.money.api.util.logging.Log;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;
import java.io.InputStream;

import static com.yandex.money.api.util.Common.checkNotNull;

/**
 * Implementation of {@link HttpClientResponse} for OkHttp.
 */
final class OkHttpClientResponse implements HttpClientResponse {

    private final Response response;
    private final boolean debug;

    OkHttpClientResponse(Response response, boolean debug) {
        this.response = checkNotNull(response, "response");
        this.debug = debug;
    }

    @Override
    public int getCode() {
        return response.code();
    }

    @Override
    public String getMessage() {
        return response.message();
    }

    @Override
    public String getUrl() {
        return response.request().url().toString();
    }

    @Override
    public String getHeader(String name) {
        return response.header(name);
    }

    @Override
    public String getBody() throws IOException {
        ResponseBody body = response.body();
        if (body == null) {
            Log.i("body is empty");
            return null;
        }

        String data = body.string();
        Log.i(data);
        return data;
    }

    @Override
    public InputStream getByteStream() {
        ResponseBody body = response.body();
        if (body == null) {
            Log.i("body is empty");
            return null;
        }

        InputStream stream = body.byteStream();
        return debug ? new ResponseLoggingInputStream(stream) : stream;
    }
}
