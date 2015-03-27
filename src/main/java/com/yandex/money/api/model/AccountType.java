package com.yandex.money.api.model;

/**
 * Account's type.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public enum AccountType {
    /**
     * Personal.
     */
    PERSONAL("personal"),
    /**
     * Professional.
     */
    PROFESSIONAL("professional"),
    /**
     * Unknown.
     */
    UNKNOWN("unknown");

    public final String code;

    AccountType(String code) {
        this.code = code;
    }

    public static AccountType parse(String code) {
        if (code == null) {
            return null;
        }
        for (AccountType value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return UNKNOWN;
    }
}
