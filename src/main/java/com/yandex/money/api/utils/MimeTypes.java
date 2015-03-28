package com.yandex.money.api.utils;

/**
 * This is not complete list of types.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public final class MimeTypes {

    private MimeTypes() {
        // prevents instantiating of this class
    }

    public static final class Application {

        public static final String JSON = "application/json";
        public static final String X_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
        public static final String XML = "application/xml";

        private Application() {
            // prevents instantiating of this class
        }
    }

    public static final class Text {
        public static final String XML = "text/xml";

        private Text() {
            // prevents instantiating of this class
        }
    }
}
