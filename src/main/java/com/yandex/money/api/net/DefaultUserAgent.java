package com.yandex.money.api.net;

import com.yandex.money.api.utils.Strings;

/**
 *  Default implementation of {@link com.yandex.money.api.net.UserAgent} interface.
 */
public class DefaultUserAgent implements UserAgent {

    private final String platform;

    public DefaultUserAgent(String platform) {
        if (Strings.isNullOrEmpty(platform)) {
            throw new IllegalArgumentException("platform is null or empty");
        }
        this.platform = platform;
    }

    @Override
    public String getName() {
        return "Yandex.Money.SDK/" + platform;
    }
}
