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

import com.yandex.money.api.model.Error;
import com.yandex.money.api.util.UrlEncodedUtils;

import java.net.URISyntaxException;
import java.util.Map;

/**
 * This is a convenience class to parse OAuth2 response after user authentication.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 * @see com.yandex.money.api.authorization.AuthorizationData
 */
public class AuthorizationCodeResponse {

    /**
     * temporary access token to request OAuth2 access token
     * @see com.yandex.money.api.methods.Token
     */
    public final String code;

    /**
     * error code
     */
    public final Error error;

    /**
     * human understandable error description
     */
    public final String errorDescription;

    protected AuthorizationCodeResponse(String code, Error error, String errorDescription) {
        this.code = code;
        this.error = error;
        this.errorDescription = errorDescription;
    }

    /**
     * Parses redirect URL after OAuth2 authorization and retrieves its parameters.
     *
     * @param redirectUrl redirect url
     * @return response object
     * @throws URISyntaxException if provided url is malformed
     */
    public static AuthorizationCodeResponse parse(String redirectUrl) throws URISyntaxException {
        Map<String, String> params = UrlEncodedUtils.parse(redirectUrl);
        return new AuthorizationCodeResponse(params.get("code"), Error.parse(params.get("error")),
                params.get("error_description"));
    }
}
