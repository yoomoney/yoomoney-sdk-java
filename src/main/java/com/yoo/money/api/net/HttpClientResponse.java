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

package com.yoo.money.api.net;

import java.io.IOException;
import java.io.InputStream;

/**
 * HTTP client must wrap its response in this interface.
 */
public interface HttpClientResponse {

    /**
     * Gets the HTTP status code.
     *
     * @return HTTP status code
     */
    int getCode();

    /**
     * Gets the HTTP status message.
     *
     * @return HTTP status message
     */
    String getMessage();

    /**
     * Request URL used to get a response.
     *
     * @return url
     */
    String getUrl();

    /**
     * Gets header by its name. Returns {@code null} if no value is present for this header.
     *
     * @param name name of the header
     * @return value or {@code null} if no header found for this name
     */
    String getHeader(String name);

    /**
     * Returns body as UTF-8 string.
     *
     * @return body
     * @throws IOException if unable to read response stream
     */
    String getBody() throws IOException;

    /**
     * Gets byte stream of the response.
     *
     * @return byte stream
     */
    InputStream getByteStream();
}
