package com.yandex.money.api.utils;

/**
 * Common patterns.
 *
 * @author Slava Yasevich (yasevich.slava@gmail.com)
 */
public final class Patterns {

    /**
     * Strings match this pattern are accounts.
     */
    public static final String ACCOUNT = "41\\d{9,31}";

    /**
     * Strings match this pattern are phones.
     */
    public static final String PHONE = "(\\+[0-9]+[\\- \\.]*)?(\\([0-9]+\\)[\\- \\.]*)?" +
            "([0-9][0-9\\- \\.]+[0-9])";

    /**
     * Strings match this pattern are Yandex accounts.
     */
    public static final String YANDEX = "[a-zA-Z0-9\\+\\._%\\-\\+]{1,256}";

    /**
     * Strings match this pattern are emails.
     */
    public static final String EMAIL = "[a-zA-Z0-9\\+\\._%\\-\\+]{1,256}@[a-zA-Z0-9]" +
            "[a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+";

    private Patterns() {
    }
}
