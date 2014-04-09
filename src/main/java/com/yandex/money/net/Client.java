package com.yandex.money.net;

import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;

/**
 * Http-client for request execution
 */
public class Client {

    private OkHttpClient okHttpClient;
    private boolean debugLogging = false;

    public Client(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public void setDebugLogging(boolean debugLogging) {
        this.debugLogging = debugLogging;
    }

    private static final String USER_AGENT = "yandex-money-sdk-java";

    public <T> T perform(IRequest<T> request) throws IOException {
        if (request == null) {
            throw new IllegalArgumentException("request is null");
        }

        HttpURLConnection connection = okHttpClient.open(request.requestURL());

        OutputStream out = null;
        InputStream in = null;

        try {
            setupCon(connection);

            PostRequestBodyBuffer parameters = request.buildParameters();
            if (parameters != null) {
                parameters.setHttpHeaders(connection);
            }
            request.writeHeaders(connection);

            if (parameters != null) {
                out = connection.getOutputStream();
                parameters.write(out);
                out.close();
                out = null;
            }

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                checkJSONType(connection);
                in = Utils.getBodyStream(connection, debugLogging);
                return request.parseResponse(in);
            } else {
                throw new IOException(connection.getResponseMessage());
            }

        } finally {
            // Clean up.
            if (out != null) out.close();
            if (in != null) in.close();
        }
    }

    private void setupCon(URLConnection connection) {
        connection.setRequestProperty("User-Agent", USER_AGENT);

    }

    private void checkJSONType(HttpURLConnection connection) throws IOException {
        String lengthHeader = connection.getHeaderField("Content-Length");
        if (lengthHeader != null) {
            int contentLength = Integer.valueOf(lengthHeader);
            if (contentLength > 0) {
                String header = connection.getHeaderField("Content-Type");
                if (header == null || !header.startsWith(PostRequestBodyBuffer.CONTENT_TYPE_POST_JSON)) {
                    String message = "Server has respond by wrong Content-Type:\n" + Utils.getStatusLine(connection) + "\nContent-Type: " + header;
//                    Log.w(TAG, message);
                    Utils.readBody2DevNull(connection);
                    throw new IOException(message);
                }
            }
        }
    }

    public static class Utils {

        public static String getStatusLine(HttpURLConnection connection) {
            try {
                return "HTTP " + connection.getResponseCode() + " " + connection.getResponseMessage();
            } catch (IOException e) {
                return "UNKNOWN: " + e.toString();
            }
        }

        public static void readBody2DevNull(HttpURLConnection connection) throws IOException {
            readBody2DevNull(getBodyStream(connection, false));
        }

        public static InputStream getBodyStream(HttpURLConnection connection, boolean isDebugLogging) throws IOException {
            if (connection.getResponseCode() >= 400) {
                return isDebugLogging ? new WireLoggingInputStream(connection.getErrorStream()) : connection.getErrorStream();
            } else {
                return isDebugLogging ? new WireLoggingInputStream(connection.getInputStream()) : connection.getInputStream();
            }
        }

        public static void readBody2DevNull(InputStream inputStream) throws IOException {
            try {
                if (inputStream != null) {
                    byte[] buffer = new byte[4096];
                    //noinspection StatementWithEmptyBody
                    while (inputStream.read(buffer) >= 0) {
                    }
                }
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        }

    }
}
