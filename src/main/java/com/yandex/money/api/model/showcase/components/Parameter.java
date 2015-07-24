package com.yandex.money.api.model.showcase.components;

/**
 * Parameter.
 * TODO: think about merge this interface with
 * {@link com.yandex.money.api.model.showcase.components.uicontrol.ParameterControl}.
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
        CURRENT_USER_ACCOUNT("currentuser_accountkey"),

        /**
         * Next month.
         */
        CALENDAR_NEXT_MONTH("calendar_next_month"),

        /**
         * User's email.
         */
        CURRENT_USER_EMAIL("currentuser_email");

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

        @Override
        public String toString() {
            return "AutoFill{" +
                    "code='" + code + '\'' +
                    "}";
        }
    }
}
