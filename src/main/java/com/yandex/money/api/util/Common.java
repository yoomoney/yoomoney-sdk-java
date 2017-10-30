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

package com.yandex.money.api.util;

import java.util.Collection;
import java.util.Map;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 * @author Slava Yaesvich (vyasevich@yamoney.ru)
 */
public final class Common {

    private Common() {
    }

    /**
     * Checks if value is {@code null}. Throws {@link NullPointerException} if value is {@code null}.
     *
     * @param value value to check
     * @param name name of the value
     * @param <T> type of the value
     * @return value, if check was successful
     */
    public static <T> T checkNotNull(T value, String name) {
        if (value == null) {
            throw new NullPointerException(name + " is null");
        }
        return value;
    }

    /**
     * Checks if value is {@code null} or empty. Throws {@link NullPointerException} if value is {@code null}. Throws
     * {@link IllegalArgumentException} if value is empty.
     *
     * @param value value to check
     * @param name name of the value
     * @return value, if check was successful
     */
    public static String checkNotEmpty(String value, String name) {
        if (checkNotNull(value, name).length() == 0) {
            throw new IllegalArgumentException(name + " is empty");
        }
        return value;
    }

    /**
     * Checks if collection is {@code null} or empty. Throws {@link NullPointerException} if value is {@code null}.
     * Throws {@link IllegalArgumentException} if value is empty.
     *
     * @param value collection
     * @param name name of the collection
     * @param <V> type of elements in the collection
     * @param <T> collection's type
     * @return collection, if check was successful
     */
    public static <V, T extends Collection<V>> T checkNotEmpty(T value, String name) {
        if (checkNotNull(value, name).isEmpty()) {
            throw new IllegalArgumentException(name + " is empty");
        }
        return value;
    }

    /**
     * Checks if map is {@code null} or empty. Throws {@link NullPointerException} if value is {@code null}. Throws
     * {@link IllegalArgumentException} if value is empty.
     *
     * @param value map
     * @param name name of the map
     * @param <V> type of elements in the map
     * @param <K> key of elements in the map
     * @param <T> map's type
     * @return map, if check was successful
     */
    public static <V, K, T extends Map<V, K>> T checkNotEmpty(T value, String name) {
        if (checkNotNull(value, name).isEmpty()) {
            throw new IllegalArgumentException(name + " is empty");
        }
        return value;
    }
}
