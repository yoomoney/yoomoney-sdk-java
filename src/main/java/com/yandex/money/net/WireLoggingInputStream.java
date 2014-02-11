package com.yandex.money.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

/**
 * Logging wrapper for server responses
 * Suppose, that responses in UTF-8
 */
public final class WireLoggingInputStream extends InputStream {

    private static Logger LOG = Logger.getLogger(WireLoggingInputStream.class.getName());

    final InputStream inputStream;
    final ByteArrayOutputStream buffer;

    public WireLoggingInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        buffer = new ByteArrayOutputStream();
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
        LOG.info(buffer.toString("UTF-8"));
    }
}
