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

import com.yandex.money.api.exceptions.ResourceNotFoundException;
import com.yandex.money.api.typeadapters.TypeAdapter;
import com.yandex.money.api.util.HttpHeaders;
import org.joda.time.DateTime;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static com.yandex.money.api.util.Common.checkNotNull;
import static com.yandex.money.api.util.Responses.parseDateHeader;
import static com.yandex.money.api.util.Responses.processError;

/**
 * @author Slava Yasevich
 */
public abstract class DocumentApiRequest<T> extends BaseApiRequest<HttpResourceResponse<T>> {

    private final TypeAdapter<T> typeAdapter;

    public DocumentApiRequest(TypeAdapter<T> typeAdapter) {
        this.typeAdapter = checkNotNull(typeAdapter, "typeAdapter");
    }

    @Override
    public final Method getMethod() {
        return Method.GET;
    }

    @Override
    public final HttpResourceResponse<T> parse(HttpClientResponse response) throws Exception {
        InputStream inputStream = null;
        try {
            int responseCode = response.getCode();
            switch (responseCode) {
                case HttpURLConnection.HTTP_OK:
                case HttpURLConnection.HTTP_NOT_MODIFIED:
                    DateTime lastModified = parseDateHeader(response, HttpHeaders.LAST_MODIFIED);
                    DateTime expires = parseDateHeader(response, HttpHeaders.EXPIRES);

                    HttpResourceResponse.ResourceState resourceState =
                            HttpResourceResponse.ResourceState.NOT_MODIFIED;
                    T document = null;
                    String contentType = null;

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        resourceState = HttpResourceResponse.ResourceState.DOCUMENT;
                        contentType = response.getHeader(HttpHeaders.CONTENT_TYPE);
                        inputStream = response.getByteStream();
                        document = typeAdapter.fromJson(inputStream);
                    }

                    return new HttpResourceResponse<>(resourceState, contentType, lastModified, expires, document);
                case HttpURLConnection.HTTP_NOT_FOUND:
                    throw new ResourceNotFoundException(response.getUrl());
                default:
                    throw new IOException(processError(response));
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
