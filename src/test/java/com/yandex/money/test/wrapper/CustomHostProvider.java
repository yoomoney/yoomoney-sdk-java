package com.yandex.money.test.wrapper;

import com.yandex.money.api.net.HostsProvider;

/**
 * Created by akintsev on 6/14/2016.
 */
public class CustomHostProvider extends HostsProvider {

    private final String moneyHostUrl;

    public CustomHostProvider(String moneyHostUrl) {
        super(false);

        this.moneyHostUrl = moneyHostUrl;
    }

    @Override
    public String getMoney() {
        return moneyHostUrl;
    }
}
