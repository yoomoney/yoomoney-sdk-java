package com.yandex.money.api.utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Enum of allowed languages. For CIS countries: Russian is default language.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public enum Language {
    ENGLISH("en"),
    RUSSIAN("ru");

    private static final Set<String> cisIso6391Codes;
    static {
        Set<String> codes = new HashSet<>();
        codes.add("ab");
        codes.add("az");
        codes.add("hy");
        codes.add("by");
        codes.add("bg");
        codes.add("ge");
        codes.add("kk");
        codes.add("ky");
        codes.add("lv");
        codes.add("lt");
        codes.add("mo");
        codes.add("pl");
        codes.add("ru");
        codes.add("ro");
        codes.add("tg");
        codes.add("tk");
        codes.add("uz");
        codes.add("uk");
        codes.add("et");
        cisIso6391Codes = Collections.unmodifiableSet(codes);
    }

    private final String iso6391Code;

    private Language(String iso6391Code) {
        this.iso6391Code = iso6391Code;
    }

    /**
     * @return default language based on {@link java.util.Locale#getDefault()}
     */
    public static Language getDefault() {
        Locale locale = Locale.getDefault();
        for (Language value : values()) {
            if (value.iso6391Code.equals(locale.getLanguage())) {
                return value;
            }
        }
        return getSupported(locale.getLanguage());
    }

    /**
     * @param iso6391Code ISO-639-1 code
     * @return preferred language for specified code
     */
    public static Language getSupported(String iso6391Code) {
        if (Strings.isNullOrEmpty(iso6391Code)) {
            throw new IllegalArgumentException("ISO-639-1 code can not be null or empty");
        }
        for (Language value : values()) {
            if (value.iso6391Code.equals(iso6391Code)) {
                return value;
            }
        }
        return isCis(iso6391Code) ? RUSSIAN : ENGLISH;
    }

    public String getIso6391Code() {
        return iso6391Code;
    }

    private static boolean isCis(String iso6391Code) {
        return cisIso6391Codes.contains(iso6391Code);
    }
}
