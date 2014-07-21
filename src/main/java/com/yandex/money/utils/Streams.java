package com.yandex.money.utils;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public final class Streams {

    private static final int BUFFER_SIZE = 0x1000;

    private Streams() {
    }

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
