package com.yandex.money.model.cps.misc;

public enum AccountType {
    PERSONAL("personal"),
    PROFESSIONAL("professional"),
    UNKNOWN("unknown");

    private final String accountType;

    AccountType(String accountType) {
        this.accountType = accountType;
    }

    public static AccountType parse(String accountType) {
        for (AccountType type : values()) {
            if (type.accountType.equalsIgnoreCase(accountType)) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
