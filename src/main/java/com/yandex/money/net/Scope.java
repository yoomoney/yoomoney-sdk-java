package com.yandex.money.net;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public enum Scope {
    ACCOUNT_INFO("account-info"),
    INCOMING_TRANSFERS("incoming-transfers"),
    MONEY_SOURCE("money-source"),
    OPERATION_DETAILS("operation-details"),
    OPERATION_HISTORY("operation-history"),
    PAYMENT("payment"),
    PAYMENT_P2P("payment-p2p"),
    PAYMENT_SHOP("payment-shop");

    private final String code;

    private Scope(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
