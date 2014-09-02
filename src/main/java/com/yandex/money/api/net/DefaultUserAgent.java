package com.yandex.money.api.net;

import com.yandex.money.api.utils.Strings;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
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
