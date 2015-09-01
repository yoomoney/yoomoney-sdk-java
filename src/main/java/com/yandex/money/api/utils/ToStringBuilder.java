package com.yandex.money.api.utils;

/**
 * Utility class for {@code toString} method definition.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class ToStringBuilder {

    private static final String DELIMITER = ", ";
    private static final String EQUALS = "=";

    private final StringBuilder buffer = new StringBuilder();

    private String name;
    private String prefix = "";

    public ToStringBuilder(String name) {
        checkName(name);
        this.name = name;
    }

    public <T> ToStringBuilder append(String name, T value) {
        return value == null ? appendKeyValue(name, "null") :
                appendKeyValueWithQuotes(name, value.toString());
    }

    public ToStringBuilder append(String name, boolean value) {
        return appendKeyValue(name, Boolean.toString(value));
    }

    public ToStringBuilder setName(String name) {
        checkName(name);
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return this.name + "{" + buffer.toString() + "}";
    }

    private ToStringBuilder appendKeyValue(String key, String value) {
        return appendPrefix()
                .append(key)
                .append(EQUALS)
                .append(value);
    }

    private ToStringBuilder appendKeyValueWithQuotes(String key, String value) {
        final char quote = '\'';
        return appendPrefix()
                .append(key)
                .append(EQUALS)
                .append(quote)
                .append(value)
                .append(quote);
    }

    private ToStringBuilder append(String str) {
        buffer.append(str);
        return this;
    }

    private ToStringBuilder append(char ch) {
        buffer.append(ch);
        return this;
    }

    private ToStringBuilder appendPrefix() {
        buffer.append(prefix);
        prefix = DELIMITER;
        return this;
    }

    private void checkName(String name) {
        if (Strings.isNullOrEmpty(name)) {
            throw new NullPointerException("name is null or empty");
        }
    }
}
