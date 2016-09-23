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

package com.yandex.money.api.model.showcase.components;

import com.yandex.money.api.util.Enums;

/**
 * Parameter.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public interface Parameter {

    /**
     * @return name
     */
    String getName();

    /**
     * @return value
     */
    String getValue();

    /**
     * Sets a value.
     *
     * @param value the value
     */
    void setValue(String value);

    /**
     * Auto fill macros.
     */
    enum AutoFill implements Enums.WithCode<AutoFill> {

        /**
         * User's name.
         */
        CURRENT_USER_ACCOUNT("currentuser_accountkey"),

        /**
         * Next month.
         */
        CALENDAR_NEXT_MONTH("calendar_next_month"),

        /**
         * User's email.
         */
        CURRENT_USER_EMAIL("currentuser_email");

        public final String code;

        AutoFill(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public AutoFill[] getValues() {
            return values();
        }

        /**
         * Parses incoming code to matched enum item.
         *
         * @param code value to parse.
         * @return enum item or {@code null}.
         */
        public static AutoFill parse(String code) {
            return Enums.parse(CURRENT_USER_ACCOUNT, code);
        }
    }
}
