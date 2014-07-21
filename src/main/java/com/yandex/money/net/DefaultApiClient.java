package com.yandex.money.net;

import com.squareup.okhttp.OkHttpClient;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class DefaultApiClient implements ApiClient {

    private static final long DEFAULT_TIMEOUT = 30;

    private final OkHttpClient httpClient;
    private final HostsProvider hostsProvider;

    public DefaultApiClient() {
        httpClient = new OkHttpClient();
        httpClient.setReadTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClient.setConnectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        hostsProvider = new HostsProvider(false);
    }

    @Override
    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    @Override
    public HostsProvider getHostsProvider() {
        return hostsProvider;
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
