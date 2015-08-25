package com.yandex.money.api.model.showcase.components;

import com.yandex.money.api.utils.ToStringBuilder;

/**
 * Base entity of payment form. All components have appropriate builders and should be
 * constructed by them.
 *
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public abstract class Component {

    public final Type type;

    protected Component(Component.Builder builder) {
        this.type = builder.type;
    }

    @Override
    public String toString() {
        return getToStringBuilder().toString();
    }

    protected abstract ToStringBuilder getToStringBuilder();

    /**
     * Possible field types.
     */
    public enum Type {

        TEXT("text"),
        NUMBER("number"),
        AMOUNT("amount"),
        EMAIL("email"),
        TEL("tel"),
        CHECKBOX("checkbox"),
        DATE("date"),
        MONTH("month"),
        SELECT("select"),
        TEXT_AREA("textarea"),
        SUBMIT("submit"),
        PARAGRAPH("p"),
        GROUP("group");

        public final String code;

        Type(String code) {
            this.code = code;
        }

        public static Type parse(String code) {
            for (Type type : values()) {
                if (type.code.equalsIgnoreCase(code)) {
                    return type;
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return "Type{" +
                    "code='" + code + '\'' +
                    "} ";
        }
    }

    public static abstract class Builder {

        private Component.Type type;

        public final Builder setType(Component.Type type) {
            this.type = type;
            return this;
        }

        public abstract Component create();
    }
}
