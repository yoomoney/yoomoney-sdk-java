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

import com.yandex.money.api.util.logging.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.yandex.money.api.util.Common.checkNotNull;

/**
 * Logging wrapper for server responses
 * Supposes, that responses in UTF-8
 */
final class ResponseLoggingInputStream extends InputStream {

    private static final String TAG = ResponseLoggingInputStream.class.getName();

    private final InputStream inputStream;
    private final ByteArrayOutputStream buffer;

    ResponseLoggingInputStream(InputStream inputStream) {
        this.inputStream = checkNotNull(inputStream, "input stream");
        this.buffer = new ByteArrayOutputStream();
    }

    @Override
    public int read() throws IOException {
        int c = inputStream.read();
        if (c > -1) {
            buffer.write((byte) c);
        }
        return c;
    }

    @Override
    public int read(byte b[]) throws IOException {
        int read = inputStream.read(b);
        if (read > -1) {
            buffer.write(b, 0, read);
        }
        return read;
    }

    @Override
    public int read(byte b[], int off, int len) throws IOException {
        int read = inputStream.read(b, off, len);
        if (read > -1) {
            buffer.write(b, off, read);
        }
        return read;
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
        buffer.close();
        Log.i("response: " + buffer.toString("UTF-8"));
    }
}
