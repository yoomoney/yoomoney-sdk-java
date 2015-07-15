package com.yandex.money.api.model.showcase.components;

/**
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public abstract class Component {

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

        public static Type parse(String typeName) {
            for (Type type : values()) {
                if (type.code.equalsIgnoreCase(typeName)) {
                    return type;
                }
            }
            return null;
        }

        Type(String code) {
            this.code = code;
        }
    }

    public static abstract class Builder {
        public abstract Component create();
    }
}
