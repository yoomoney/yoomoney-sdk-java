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

    public <T> T perform(IRequest<T> IRequest) throws IOException {
        HttpURLConnection connection = okHttpClient.open(IRequest.requestURL());

        OutputStream out = null;
        InputStream in = null;

        try {

            return IRequest.parseResponse(in);
        } finally {
            // Clean up.
            if (out != null) out.close();
            if (in != null) in.close();
        }
    }
}
