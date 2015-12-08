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

import static com.yandex.money.api.utils.Common.checkNotNull;

/**
 * @author Slava Yasevich
 */
public final class Enums {

    /**
     * Parses {@code code} to using {@code prototype} to get {@code enum}'s declaration.
     *
     * Returns {@code null} if found no matches for specified {@code code}.
     *
     * @param prototype prototype to use
     * @param code      code to parse
     * @param <T>       type of {@code enum}
     * @return          {@code enum}'s value
     */
    public static <T extends WithCode<T>> T parse(T prototype, String code) {
        return parse(prototype, null, code);
    }

    /**
     * Parses {@code code} to using {@code prototype} to get {@code enum}'s declaration.
     *
     * Throws {@link NullPointerException} if found no matches for specified {@code code}.
     *
     * @param prototype prototype to use
     * @param code      code to parse
     * @param <T>       type of {@code enum}
     * @return          {@code enum}'s value
     */
    public static <T extends WithCode<T>> T parseOrThrow(T prototype, String code) {
        return checkNotNull(parse(prototype, null, code), "value");
    }

    /**
     * Parses {@code code} to using {@code prototype} to get {@code enum}'s declaration.
     *
     * Returns {@code defaultValue} if found no matches for specified {@code code}.
     *
     * @param prototype    prototype to use
     * @param defaultValue default value
     * @param code         code to parse
     * @param <T>          type of {@code enum}
     * @return             {@code enum}'s value
     */
    public static <T extends WithCode<T>> T parse(T prototype, T defaultValue, String code) {
        if (code == null) {
            return defaultValue;
        }
        for (T value : checkNotNull(prototype, "prototype").getValues()) {
            if (code.equals(value.getCode())) {
                return value;
            }
        }
        return defaultValue;
    }

    public interface WithCode<T> {
        String getCode();
        T[] getValues();
    }
}
