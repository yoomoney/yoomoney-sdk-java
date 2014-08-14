package com.yandex.money.api.model;

/**
 * Wallet. It used for payments from user's account.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class Wallet extends MoneySource {
    public Wallet() {
        super("wallet");
    }
}
