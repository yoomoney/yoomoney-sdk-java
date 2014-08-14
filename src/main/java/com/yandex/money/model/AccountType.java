package com.yandex.money.model;

public enum AccountType {
    PERSONAL("personal"),
    PROFESSIONAL("professional"),
    UNKNOWN("unknown");

    private final String code;

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

    public String getCode() {
        return code;
    }
}
