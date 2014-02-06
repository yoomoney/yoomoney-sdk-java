package com.yandex.money.net;

import com.squareup.okhttp.OkHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * Http-клиент для вызова запросов
 */
public class Client {

    private OkHttpClient okHttpClient;

    public Client(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    private static final String USER_AGENT = "ym-java-cps-sdk";

    public <T> T perform(IRequest<T> request) throws IOException {
        if (request == null) {
            throw new IllegalArgumentException("request is null");
        }

        HttpURLConnection connection = okHttpClient.open(request.requestURL());

        OutputStream out = null;
        InputStream in = null;

        try {
            connection.setRequestProperty("User-Agent", USER_AGENT);

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

            return request.parseResponse(in);
        } finally {
            // Clean up.
            if (out != null) out.close();
            if (in != null) in.close();
        }
    }
}
