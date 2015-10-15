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

package com.yandex.money.api.model;

import com.yandex.money.api.utils.Patterns;

/**
 * Type of payee identifier.
 * <p/>
 * Provides convenience methods to determine the type.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public enum PayeeIdentifierType {
    /**
     * Account number.
     */
    ACCOUNT("account"),
    /**
     * Phone number.
     */
    PHONE("phone"),
    /**
     * Email address.
     */
    EMAIL("email"),
    /**
     * Unknown identifier.
     */
    NULL(null);

    public final String code;

    PayeeIdentifierType(String code) {
        this.code = code;
    }

    public static PayeeIdentifierType parse(String code) {
        if (code == null) {
            return NULL;
        }
        for (PayeeIdentifierType value : values()) {
            if (code.equals(value.code)) {
                return value;
            }
        }
        return NULL;
    }

    /**
     * Determines identifier type by identifier.
     *
     * @param identifier the identifier
     * @return type
     */
    public static PayeeIdentifierType determine(String identifier) {
        if (identifier == null || identifier.isEmpty()) {
            return NULL;
        }

        if (identifier.matches(Patterns.ACCOUNT)) {
            return ACCOUNT;
        } else if (identifier.matches(Patterns.PHONE)) {
            return PHONE;
        } else if (identifier.matches(Patterns.YANDEX) || identifier.matches(Patterns.EMAIL)) {
            return EMAIL;
        } else {
            return NULL;
        }
    }
}
