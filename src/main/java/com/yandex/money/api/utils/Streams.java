package com.yandex.money.api.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Implements common streams operation.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public final class Streams {

    private static final int BUFFER_SIZE = 0x1000;

    private Streams() {
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
        if (stream == null) {
            throw new NullPointerException("stream is null");
        }

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
