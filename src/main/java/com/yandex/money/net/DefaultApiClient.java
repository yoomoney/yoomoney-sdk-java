package com.yandex.money.net;

import com.squareup.okhttp.OkHttpClient;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class DefaultApiClient implements ApiClient {

    private static final long DEFAULT_TIMEOUT = 30;

    @Override
    public OkHttpClient getHttpClient() {
        OkHttpClient client = new OkHttpClient();
        client.setReadTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        client.setConnectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        return client;
    }

    @Override
    public String getUserAgent() {
        return "yandex-money-sdk";
    }

    @Override
    public String getLanguage() {
        return Locale.getDefault().getLanguage();
    }
}
