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

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * API methods implement this interface.
 *
 * @param <T> response
 */
public interface ApiRequest<T> {

    /**
     * Builds URL with using specified hosts provider
     *
     * @param hostsProvider hosts provider
     * @return complete URL
     * @throws MalformedURLException if URL is malformed
     */
    URL requestURL(HostsProvider hostsProvider) throws MalformedURLException;

    /**
     * Parses API response from stream.
     *
     * @param inputStream input stream
     * @return response
     */
    T parseResponse(InputStream inputStream);

    /**
     * Builds post parameters to use when posting a request. Can be null.
     *
     * @return buffer of parameters
     */
    PostRequestBodyBuffer buildParameters() throws IOException;
}
