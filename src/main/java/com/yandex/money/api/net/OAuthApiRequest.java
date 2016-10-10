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

import com.yandex.money.api.exceptions.InsufficientScopeException;
import com.yandex.money.api.exceptions.InvalidRequestException;
import com.yandex.money.api.exceptions.InvalidTokenException;
import com.yandex.money.api.typeadapters.TypeAdapter;
import com.yandex.money.api.util.Common;
import com.yandex.money.api.util.HttpHeaders;
import com.yandex.money.api.util.MimeTypes;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static com.yandex.money.api.util.Responses.processError;

/**
 * @author Slava Yasevich
 */
public abstract class OAuthApiRequest<T> extends BaseApiRequest<T> {

    private final TypeAdapter<T> typeAdapter;

    public OAuthApiRequest(TypeAdapter<T> typeAdapter) {
        this.typeAdapter = Common.checkNotNull(typeAdapter, "typeAdapter");
    }

    @Override
    public final Method getMethod() {
        return Method.POST;
    }

    @Override
    public final T parse(HttpClientResponse response) throws Exception {
        InputStream inputStream = null;

        try {
            switch (response.getCode()) {
                case HttpURLConnection.HTTP_OK:
                case HttpURLConnection.HTTP_ACCEPTED:
                case HttpURLConnection.HTTP_BAD_REQUEST:
                    inputStream = response.getByteStream();
                    if (isJsonType(response)) {
                        return typeAdapter.fromJson(inputStream);
                    } else {
                        throw new InvalidRequestException(processError(response));
                    }
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    throw new InvalidTokenException(processError(response));
                case HttpURLConnection.HTTP_FORBIDDEN:
                    throw new InsufficientScopeException(processError(response));
                default:
                    throw new IOException(processError(response));
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    private static boolean isJsonType(HttpClientResponse response) {
        String field = response.getHeader(HttpHeaders.CONTENT_TYPE);
        return field != null && (field.startsWith(MimeTypes.Application.JSON) || field.startsWith(MimeTypes.Text.JSON));
    }
}
