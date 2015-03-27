package com.yandex.money.api.model;

import com.yandex.money.api.utils.Patterns;

/**
 * Type of payee identifier.
 * <p/>
 * Provides convenience methods to determine the type.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public enum PayeeIdentifierType {
    /**
     * Account number.
     */
    ACCOUNT("account"),
    /**
     * Phone number.
     */
    PHONE("phone"),
    /**
     * Email address.
     */
    EMAIL("email"),
    /**
     * Unknown identifier.
     */
    UNKNOWN("unknown");

    public final String code;

    PayeeIdentifierType(String code) {
        this.code = code;
    }

    public static PayeeIdentifierType parse(String identifier) {
        if (identifier == null) {
            return null;
        }
        for (PayeeIdentifierType value : values()) {
            if (value.code.equals(identifier)) {
                return value;
            }
        }
        return UNKNOWN;
    }

    /**
     * Determines identifier type by identifier.
     *
     * @param identifier the identifier
     * @return type
     */
    public static PayeeIdentifierType determine(String identifier) {
        if (identifier == null || identifier.isEmpty()) {
            return UNKNOWN;
        }

        if (identifier.matches(Patterns.ACCOUNT)) {
            return ACCOUNT;
        } else if (identifier.matches(Patterns.PHONE)) {
            return PHONE;
        } else if (identifier.matches(Patterns.YANDEX) || identifier.matches(Patterns.EMAIL)) {
            return EMAIL;
        } else {
            return UNKNOWN;
        }
    }
}
