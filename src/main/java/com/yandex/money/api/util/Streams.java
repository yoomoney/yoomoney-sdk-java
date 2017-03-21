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

package com.yandex.money.api.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.yandex.money.api.util.Common.checkNotNull;

/**
 * Implements common streams operation.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
@Deprecated
public final class Streams {

    private static final int BUFFER_SIZE = 0x1000;

    private Streams() {
        // prevents instantiating of this class
    }

    /**
     * Copies input stream to output stream.
     *
     * @param from source
     * @param to target
     * @return bytes copied
     */
    public static long copy(InputStream from, OutputStream to) throws IOException {
        byte[] buf = new byte[BUFFER_SIZE];
        long total = 0;
        while (true) {
            int r = from.read(buf);
            if (r == -1) {
                break;
            }
            to.write(buf, 0, r);
            total += r;
        }
        return total;
    }

    /**
     * Just reads input stream. No target.
     *
     * @param stream source
     */
    public static void readStreamToNull(InputStream stream) throws IOException {
        checkNotNull(stream, "stream");

        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            //noinspection StatementWithEmptyBody
            while (stream.read(buffer) > 0) {
            }
        } finally {
            stream.close();
        }
    }
}
