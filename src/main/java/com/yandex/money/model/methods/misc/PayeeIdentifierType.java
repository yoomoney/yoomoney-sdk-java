package com.yandex.money.model.methods.misc;

public enum PayeeIdentifierType {
    ACCOUNT("account"),
    PHONE("phone"),
    EMAIL("email"),
    UNKNOWN("unknown");

    private static final String ACCOUNT_PATTERN = "41\\d{9,31}";
    private static final String PHONE_PATTERN = "(\\+[0-9]+[\\- \\.]*)?(\\([0-9]+\\)[\\- \\.]*)?" +
            "([0-9][0-9\\- \\.]+[0-9])";
    private static final String YANDEX_PATTERN = "[a-zA-Z0-9\\+\\._%\\-\\+]{1,256}";
    private static final String EMAIL_PATTERN = "[a-zA-Z0-9\\+\\._%\\-\\+]{1,256}@[a-zA-Z0-9]" +
            "[a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+";

    private final String identifier;

    private PayeeIdentifierType(String identifier) {
        this.identifier = identifier;
    }

    public static PayeeIdentifierType parse(String identifier) {
        if (identifier == null) {
            return null;
        }
        for (PayeeIdentifierType value : values()) {
            if (value.identifier.equals(identifier)) {
                return value;
            }
        }
        return UNKNOWN;
    }

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
}
