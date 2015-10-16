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

import java.util.Collection;
import java.util.Map;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 * @author Slava Yaesvich (vyasevich@yamoney.ru)
 */
public final class Common {

    private Common() {
    }

    public static void checkNotNull(Object value, String name) {
        if (value == null) {
            throw new NullPointerException(name + " is null");
        }
    }

    public static void checkNotEmpty(String value, String name) {
        if (Strings.isNullOrEmpty(value)) {
            throw new IllegalArgumentException(name + " is null or empty");
        }
    }

    public static void checkNotEmpty(Collection<?> value, String name) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(name + " is null or empty");
        }
    }

    public static void checkNotEmpty(Map<?, ?> value, String name) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException(name + " is null or empty");
        }
    }
}
