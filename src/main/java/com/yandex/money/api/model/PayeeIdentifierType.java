package com.yandex.money.api.model;

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

    private static final String ACCOUNT_PATTERN = "41\\d{9,31}";
    private static final String PHONE_PATTERN = "(\\+[0-9]+[\\- \\.]*)?(\\([0-9]+\\)[\\- \\.]*)?" +
            "([0-9][0-9\\- \\.]+[0-9])";
    private static final String YANDEX_PATTERN = "[a-zA-Z0-9\\+\\._%\\-\\+]{1,256}";
    private static final String EMAIL_PATTERN = "[a-zA-Z0-9\\+\\._%\\-\\+]{1,256}@[a-zA-Z0-9]" +
            "[a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+";

    private final String code;

    private PayeeIdentifierType(String code) {
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

        if (identifier.matches(ACCOUNT_PATTERN)) {
            return ACCOUNT;
        } else if (identifier.matches(PHONE_PATTERN)) {
            return PHONE;
        } else if (identifier.matches(YANDEX_PATTERN) || identifier.matches(EMAIL_PATTERN)) {
            return EMAIL;
        } else {
            return UNKNOWN;
        }
    }

    public String getCode() {
        return code;
    }
}
