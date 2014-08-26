package com.yandex.money.api.net;

import com.squareup.okhttp.OkHttpClient;
import com.yandex.money.api.utils.Language;

import java.util.concurrent.TimeUnit;

/**
 * Default implementation of {@link com.yandex.money.api.net.ApiClient} interface.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class DefaultApiClient implements ApiClient {

    private static final long DEFAULT_TIMEOUT = 30;

    private final String id;
    private final OkHttpClient httpClient;
    private final HostsProvider hostsProvider;

    /**
     * Constructor.
     *
     * @param clientId client id to use
     */
    public DefaultApiClient(String clientId) {
        if (clientId == null) {
            throw new NullPointerException("client id is null");
        }
        id = clientId;
        httpClient = new OkHttpClient();
        httpClient.setReadTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        httpClient.setConnectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        hostsProvider = new HostsProvider(false);
    }

    @Override
    public String getClientId() {
        return id;
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
    public UserAgent getUserAgent() {
        return new UserAgent() {
            @Override
            public String getName() {
                return "yandex-money-sdk";
            }
        };
    }

    @Override
    public Language getLanguage() {
        return Language.getDefault();
    }
}
