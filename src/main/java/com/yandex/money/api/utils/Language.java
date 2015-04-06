/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 NBCO Yandex.Money LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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

    public final String iso6391Code;

    Language(String iso6391Code) {
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

    private static boolean isCis(String iso6391Code) {
        return cisIso6391Codes.contains(iso6391Code);
    }
}
