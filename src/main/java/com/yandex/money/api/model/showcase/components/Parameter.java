package com.yandex.money.api.model.showcase.components;

/**
 * All components which can change its value implement this interface.
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

    enum AutoFill {
        ACCOUNT("currentuser_accountkey"),
        NEXT_MONTH("calendar_next_month"),
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
