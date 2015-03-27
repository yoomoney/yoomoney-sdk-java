package com.yandex.money.api.model;

/**
 * Some money source: wallet, card, etc.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public abstract class MoneySource {

    /**
     * unique money source id
     */
    public final String id;

    /**
     * Constructor.
     *
     * @param id unique money source id
     */
    protected MoneySource(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "MoneySource{" +
                "id='" + id + '\'' +
                '}';
    }
}
