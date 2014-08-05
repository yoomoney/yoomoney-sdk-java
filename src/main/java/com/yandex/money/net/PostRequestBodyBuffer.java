package com.yandex.money.net;

import com.yandex.money.utils.HttpHeaders;
import com.yandex.money.utils.MimeTypes;
import com.yandex.money.utils.Streams;
import com.yandex.money.utils.Strings;

import org.joda.time.DateTime;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

/**
 *
 */
public class PostRequestBodyBuffer extends ByteArrayOutputStream {

    public static final Charset REQUEST_CHARSET_UTF8 = Charset.forName("UTF-8");

    private static final byte[] POST_PARAM_DELIMITER = "&".getBytes(REQUEST_CHARSET_UTF8);
    private static final byte[] POST_PARAM_NV_DELIMITER = "=".getBytes(REQUEST_CHARSET_UTF8);

    private String contentType = MimeTypes.Application.X_WWW_FORM_URLENCODED;

    public PostRequestBodyBuffer addParam(String name, int value) {
        return addParam(name, Integer.toString(value));
    }

    public PostRequestBodyBuffer addParam(String name, String value) {
        try {
            return addParamWithoutURLEncoding(
                    URLEncoder.encode(name, REQUEST_CHARSET_UTF8.name())
                            .getBytes(REQUEST_CHARSET_UTF8),
                    URLEncoder.encode(value, REQUEST_CHARSET_UTF8.name())
                            .replace("+", "%20")
                            .getBytes(REQUEST_CHARSET_UTF8));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public PostRequestBodyBuffer addParamIfNotNull(String name, Integer value) {
        return value != null ? addParam(name, value.toString()) : this;
    }

    public PostRequestBodyBuffer addParamIfNotNull(String name, Boolean value) {
        return value != null ? addParam(name, value.toString()) : this;
    }

    public PostRequestBodyBuffer addParamIfNotNull(String name, String value) {
        return value != null ? addParam(name, value) : this;
    }

    public PostRequestBodyBuffer addParamIfNotNull(String name, BigDecimal value) {
        return value != null ? addParam(name, value.toPlainString()) : this;
    }

    public PostRequestBodyBuffer addDateTimeIfNotNull(String name, DateTime value) {
        if (value != null) {
            return addParam(name, value.toString());
        }
        else return this;
    }

    public PostRequestBodyBuffer addBooleanIfTrue(String name, Boolean value) {
        if (value != null && value)
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

    public PostRequestBodyBuffer addContent(InputStream inputStream) {
        try {
            Streams.copy(inputStream, this);
            return this;
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    public PostRequestBodyBuffer setContentType(String contentType) {
        if (Strings.isNullOrEmpty(contentType)) {
            throw new IllegalArgumentException("contentType should not be null or empty");
        }
        this.contentType = contentType;
        return this;
    }

    public void setHttpHeaders(HttpURLConnection connection) throws ProtocolException {
        connection.setRequestMethod("POST");
        connection.setRequestProperty(HttpHeaders.CONTENT_TYPE, contentType);
        connection.setFixedLengthStreamingMode(size());
    }

    public void write(OutputStream out) throws IOException {
        out.write(buf, 0, count);
    }
}
