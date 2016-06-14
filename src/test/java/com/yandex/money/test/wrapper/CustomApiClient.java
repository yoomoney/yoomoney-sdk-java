package com.yandex.money.test.wrapper;

import com.yandex.money.api.net.DefaultApiClient;
import com.yandex.money.api.net.HostsProvider;

/**
 * Created by akintsev on 6/14/2016.
 */
public class CustomApiClient extends DefaultApiClient {
    private final HostsProvider customHostsProvider;


    public CustomApiClient(String clientId, String host) {
        super(clientId);

        customHostsProvider = new CustomHostProvider(host);
    }

    public CustomApiClient(String clientId, boolean debugLogging, String host) {
        super(clientId, debugLogging);

        customHostsProvider = new CustomHostProvider(host);
    }

    @Override
    public HostsProvider getHostsProvider() {
        return customHostsProvider;
    }
}
