package com.yandex.money.api.model.showcase.components;

/**
 * Parameter.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public interface Parameter {

    /**
     * Returns a name.
     *
     * @return name
     */
    String getName();

    /**
     * Returns a value.
     *
     * @return value
     */
    String getValue();

    /**
     * Sets a value.
     *
     * @param value the value
     */
    void setValue(String value);

    /**
     * Auto fill macros.
     * <p/>
     * TODO: move this to appropriate field?
     */
    enum AutoFill {

        /**
         * User's name.
         */
        ACCOUNT("CURRENT_USER_ACCOUNT"),

        /**
         * Next month.
         */
        NEXT_MONTH("CALENDAR_NEXT_MONTH"),

        /**
         * User's email.
         */
        EMAIL("CURRENT_USER_EMAIL");

        public final String code;

        AutoFill(String code) {
            this.code = code;
        }

        /**
         * Parses incoming code to matched enum item.
         *
         * @param code value to parse.
         * @return enum item or {@code null}.
         */
        public static AutoFill parse(String code) {
            for (AutoFill autoFill : AutoFill.values()) {
                if (autoFill.code.equalsIgnoreCase(code)) {
                    return autoFill;
                }
            }
            return null;
        }
    }
}
