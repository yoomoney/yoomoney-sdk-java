package com.yandex.money;

import com.squareup.okhttp.OkHttpClient;
import com.yandex.money.net.Client;
import com.yandex.money.net.Request;

import java.io.IOException;

public class YandexMoney {

    private Client client;

    public YandexMoney() {
        client = new Client(defaultHttpClient());
    }

    public YandexMoney(OkHttpClient okHttpClient) {
        this.client = new Client(okHttpClient);
    }

    public <T> T performRequest(Request<T> request) throws IOException {
        return client.perform(request);
    }

    private OkHttpClient defaultHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        // todo set params for client (timeout, etc.)
        return okHttpClient;
    }
}
