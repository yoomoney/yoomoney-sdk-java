package com.yandex.money;

import com.squareup.okhttp.OkHttpClient;
import com.yandex.money.net.Client;
import com.yandex.money.net.IRequest;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class YandexMoney {

    private Client client;

    public YandexMoney() {
        this(defaultHttpClient());
    }

    public YandexMoney(OkHttpClient okHttpClient) {
        this.client = new Client(okHttpClient);
    }

    public void setDebugLogging(boolean debugLogging) {
        client.setDebugLogging(debugLogging);
    }

    public <T> T performRequest(IRequest<T> IRequest) throws IOException {
        return client.perform(IRequest);
    }

    private static final long TIMEOUT_IN_SEC = 30;

    private static OkHttpClient defaultHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(TIMEOUT_IN_SEC, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(TIMEOUT_IN_SEC, TimeUnit.SECONDS);
        return okHttpClient;
    }
}
