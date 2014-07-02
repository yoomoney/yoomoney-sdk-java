package com.yandex.money.model.cps.misc;

public enum AccountStatus {
    ANONYMOUS("anonymous"),
    NAMED("named"),
    IDENTIFIED("identified"),
    UNKNOWN("unknown");

    private final String accountStatus;

    AccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public static AccountStatus parse(String accountStatus) {
        for (AccountStatus value : values()) {
            if (value.accountStatus.equals(accountStatus)) {
                return value;
            }
        }
        return UNKNOWN;
    }
}
