package com.yandex.money.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 *
 */
public class PostRequestBodyBuffer extends ByteArrayOutputStream {

    public static final Charset REQUEST_CHARSET_UTF8 = Charset.forName("UTF-8");

    public static final String CONTENT_TYPE_POST_FORM = "application/x-www-form-urlencoded";
    public static final String CONTENT_TYPE_POST_JSON = "application/json";

    private static final byte[] POST_PARAM_DELIMITER = "&".getBytes(REQUEST_CHARSET_UTF8);
    private static final byte[] POST_PARAM_NV_DELIMITER = "=".getBytes(REQUEST_CHARSET_UTF8);

    public PostRequestBodyBuffer addParam(String name, String value) {
        try {
            return addParamWithoutURLEncoding(
                    URLEncoder.encode(name, REQUEST_CHARSET_UTF8.name()).getBytes(REQUEST_CHARSET_UTF8),
                    URLEncoder.encode(value, REQUEST_CHARSET_UTF8.name()).getBytes(REQUEST_CHARSET_UTF8));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public PostRequestBodyBuffer addBooleanIfTrue(String name, Boolean value) {
        if (value)
            return addParam(name, "true");
        else
            return this;
    }

    public PostRequestBodyBuffer addParamWithoutURLEncoding(byte[] name, byte[] value) {
        try {
            if (size() > 0) {
                write(POST_PARAM_DELIMITER);
            }
            write(name);
            write(POST_PARAM_NV_DELIMITER);
            write(value);
            return this;
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public void setHttpHeaders(HttpURLConnection connection) throws ProtocolException {
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", CONTENT_TYPE_POST_FORM);
        connection.setFixedLengthStreamingMode(size());
    }

    public void write(OutputStream out) throws IOException {
        out.write(buf, 0, count);
    }
}
