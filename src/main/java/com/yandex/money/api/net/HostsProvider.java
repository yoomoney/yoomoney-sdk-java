package com.yandex.money.api.net;

/**
 * Provides necessary hosts. They are used to perform API requests.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class HostsProvider {

    private final boolean mobile;

    /**
     * Constructor.
     *
     * @param mobile {@code true} if running on a mobile device
     */
    public HostsProvider(boolean mobile) {
        this.mobile = mobile;
    }

    /**
     * @return {@code https://money.yandex.ru}
     */
    public String getMoney() {
        return "https://money.yandex.ru";
    }

    /**
     * @return {@code https://money.yandex.ru/api}
     */
    public String getMoneyApi() {
        return getMoney() + "/api";
    }

    /**
     * @return {@code https://sp-money.yandex.ru} or {@code https://m.sp-money.yandex.ru} based on
     * mobile parameter
     */
    public String getSpMoney() {
        return mobile ? "https://m.sp-money.yandex.ru" : "https://sp-money.yandex.ru";
    }
}
