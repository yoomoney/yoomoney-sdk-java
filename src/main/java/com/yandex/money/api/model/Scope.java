package com.yandex.money.api.model;

/**
 * Access token scopes. Used when user go through OAuth2 process.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 * @see com.yandex.money.api.net.OAuth2Authorization
 */
public enum Scope {
    /**
     * Allow get account general info (e.g. account id, balance, linked cards, etc.)
     */
    ACCOUNT_INFO("account-info"),
    /**
     * Allow accept and reject incoming transfers.
     */
    INCOMING_TRANSFERS("incoming-transfers"),
    /**
     * Available payment methods.
     */
    MONEY_SOURCE("money-source"),
    /**
     * Allow to get details of an operation.
     */
    OPERATION_DETAILS("operation-details"),
    /**
     * Allow to read operations history.
     */
    OPERATION_HISTORY("operation-history"),
    /**
     * Allow to do payments.
     */
    PAYMENT("payment"),
    /**
     * Allow to do P2P transfers.
     */
    PAYMENT_P2P("payment-p2p"),
    /**
     * Allow payments to a shop.
     */
    PAYMENT_SHOP("payment-shop");

    private final String code;

    private Scope(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
