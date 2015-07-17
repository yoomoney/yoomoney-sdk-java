package com.yandex.money.api.model.showcase.components;

/**
 * Entity which can change internal state implements this interface.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public interface Parameter {

    /**
     * Gets a name of a parameter.
     *
     * @return the name
     */
    String getName();

    /**
     * Sets a value parsing string accordingly.
     *
     * @param value the value
     */
    void setValue(String value);

    /**
     * Gets string representation of a value.
     *
     * @return the value
     */
    String getValue();

    /**
     * Possible values of auto filling macro.
     * <p/>
     * TODO: move this to appropriate field?
     */
    enum AutoFill {
        /**
         * Current user name.
         */
        ACCOUNT("currentuser_accountkey"),

        /**
         * Next month.
         */
        NEXT_MONTH("calendar_next_month"),

        /**
         * Current user's email.
         */
        EMAIL("currentuser_email");

        AutoFill(String code) {
            this.code = code;
        }

        public final String code;

        public static AutoFill parse(String macroName) {
            for (AutoFill autoFill : AutoFill.values()) {
                if (autoFill.code.equalsIgnoreCase(macroName)) {
                    return autoFill;
                }
            }
            return null;
        }
    }
}
