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
import com.yandex.money.api.typeadapters.BaseTypeAdapter;
import com.yandex.money.api.typeadapters.GsonProvider;
import com.yandex.money.api.typeadapters.TypeAdapter;
import com.yandex.money.api.util.HttpHeaders;
import com.yandex.money.api.util.MimeTypes;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import static com.yandex.money.api.util.Common.checkNotNull;
import static com.yandex.money.api.util.Responses.processError;

/**
 * <p>Base implementation of {@link ApiRequest} for API v1. Parses only JSON responses otherwise
 * {@link InvalidRequestException} is thrown.</p>
 *
 * <p>If server returns HTTP status code 200 (Ok), 202 (Accepted) or 400 (Bad request) then this object will try to
 * parse body of a response</p>
 *
 * <p>If server returns HTTP status code 401 (Unauthorized) then {@link InvalidTokenException} is thrown.</p>
 *
 * <p>If server returns HTTP status code 403 (Forbidden) then {@link InsufficientScopeException} is thrown.</p>
 *
 * <p>In other cases {@link IOException} is thrown</p>.
 */
public abstract class FirstApiRequest<T> extends BaseApiRequest<T> {

    private final Class<T> cls;
    private final BaseTypeAdapter<T> typeAdapter;

    public FirstApiRequest(Class<T> cls) {
        this.cls = checkNotNull(cls, "cls");
        this.typeAdapter = null;
    }

    /**
     * Constructor.
     *
     * @param typeAdapter type adapter to use for a response document parsing
     */
    @Deprecated
    public FirstApiRequest(TypeAdapter<T> typeAdapter) {
        this.cls = null;
        this.typeAdapter = (BaseTypeAdapter<T>) checkNotNull(typeAdapter, "typeAdapter");
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
                        return GsonProvider.getGson()
                                .fromJson(new InputStreamReader(inputStream), getType());
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

    private Class<T> getType() {
        return typeAdapter != null ? typeAdapter.getType() : cls;
    }
}
