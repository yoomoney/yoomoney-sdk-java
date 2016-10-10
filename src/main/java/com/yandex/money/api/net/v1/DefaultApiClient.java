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

package com.yandex.money.api.net.v1;

import com.yandex.money.api.authorization.AuthorizationData;
import com.yandex.money.api.authorization.AuthorizationParameters;
import com.yandex.money.api.exceptions.InsufficientScopeException;
import com.yandex.money.api.exceptions.InvalidRequestException;
import com.yandex.money.api.exceptions.InvalidTokenException;
import com.yandex.money.api.net.ApiRequest;
import com.yandex.money.api.net.BaseApiClient;
import com.yandex.money.api.util.HttpHeaders;
import com.yandex.money.api.util.MimeTypes;
import okhttp3.Response;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static com.yandex.money.api.net.ResponseHandler.getInputStream;
import static com.yandex.money.api.net.ResponseHandler.processError;

/**
 * Default client for API v1.
 */
public class DefaultApiClient extends BaseApiClient {

    protected DefaultApiClient(Builder builder) {
        super(builder);
    }

    @Override
    public AuthorizationData createAuthorizationData(AuthorizationParameters parameters) {
        parameters.add("client_id", getClientId());
        return new AuthorizationDataImpl(getHostsProvider().getWebUrl(), parameters.build());
    }

    /**
     * Synchronous execution of a request.
     *
     * @param request the request
     * @param <T> response type
     * @return parsed response
     * @throws IOException if something went wrong during IO operations
     * @throws InvalidRequestException if server responded with 404 code
     * @throws InvalidTokenException if server responded with 401 code
     * @throws InsufficientScopeException if server responded with 403 code
     */
    public <T> T execute(ApiRequest<T> request)
            throws IOException, InvalidRequestException, InvalidTokenException, InsufficientScopeException {

        Response response = call(request);
        InputStream inputStream = null;

        try {
            switch (response.code()) {
                case HttpURLConnection.HTTP_OK:
                case HttpURLConnection.HTTP_ACCEPTED:
                case HttpURLConnection.HTTP_BAD_REQUEST:
                    inputStream = getInputStream(response, isDebugEnabled());
                    if (isJsonType(response)) {
                        return request.parseResponse(inputStream);
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

    private static boolean isJsonType(Response response) {
        String field = response.header(HttpHeaders.CONTENT_TYPE);
        return field != null && (field.startsWith(MimeTypes.Application.JSON) || field.startsWith(MimeTypes.Text.JSON));
    }

    public static class Builder extends BaseApiClient.Builder {
        @Override
        public DefaultApiClient create() {
            return new DefaultApiClient(this);
        }
    }
}
