package com.yandex.money.api.model;

/**
 * Account status.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public enum AccountStatus {
    /**
     * Anonymous account.
     */
    ANONYMOUS("anonymous"),
    /**
     * Named account.
     */
    NAMED("named"),
    /**
     * Identified account.
     */
    IDENTIFIED("identified"),
    /**
     * Unknown account.
     */
    UNKNOWN("unknown");

    private final String code;

    AccountStatus(String code) {
        this.code = code;
    }

    public static AccountStatus parse(String code) {
        if (code == null) {
            return null;
        }
        for (AccountStatus value : values()) {
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
