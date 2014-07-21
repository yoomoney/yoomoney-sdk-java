package com.yandex.money.net;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class HostsProvider {

    private final boolean mobile;

    public HostsProvider(boolean mobile) {
        this.mobile = mobile;
    }

    public String getMoney() {
        return "https://money.yandex.ru";
    }

    public String getMoneyApi() {
        return getMoney() + "/api";
    }

    public String getSpMoney() {
        return mobile ? "https://m.sp-money.yandex.ru" : "https://sp-money.yandex.ru";
    }
}
