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
        for (AccountType value : values()) {
            if (value.accountType.equals(accountType)) {
                return value;
            }
        }
        return UNKNOWN;
    }
}
