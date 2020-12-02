/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 NBCO YooMoney LLC
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

package com.yoo.money.api.util;

/**
 * Common patterns.
 *
 * @author Slava Yasevich (support@yoomoney.ru)
 */
public final class Patterns {

    /**
     * Strings match this pattern are accounts.
     */
    public static final String ACCOUNT = "41\\d{9,31}";

    /**
     * Strings match this pattern are phones.
     */
    public static final String PHONE = "(\\+[0-9]+[\\- \\.]*)?(\\([0-9]+\\)[\\- \\.]*)?" +
            "([0-9][0-9\\- \\.]+[0-9])";

    /**
     * Strings match this pattern are Yandex accounts.
     */
    public static final String YANDEX = "[a-zA-Z0-9\\+\\._%\\-\\+]{1,256}";

    /**
     * Strings match this pattern are emails.
     */
    public static final String EMAIL = "[a-zA-Z0-9\\+\\._%\\-\\+]{1,256}@[a-zA-Z0-9]" +
            "[a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+";

    /**
     * Strings math this pattern are decimals.
     */
    public static final String DECIMAL = "[\\+\\-]?\\d*(\\.(\\d*)?)?";

    private Patterns() {
        // prevents instantiating of this class
    }
}
