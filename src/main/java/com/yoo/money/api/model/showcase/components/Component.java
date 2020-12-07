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

package com.yoo.money.api.model.showcase.components;

import com.yoo.money.api.util.Enums;

/**
 * Base entity of payment form. All components have appropriate builders and should be
 * constructed by them.
 *
 * @author Aleksandr Ershov (support@yoomoney.ru)
 */
public abstract class Component {

    /**
     * Validates component state.
     *
     * @return {@code true} if instance is valid and {@code false} otherwise.
     */
    public abstract boolean isValid();

    /**
     * Possible field types.
     */
    public enum Type implements Enums.WithCode<Type> {

        TEXT("text"),
        NUMBER("number"),
        AMOUNT("amount"),
        EMAIL("email"),
        TEL("tel"),
        CHECKBOX("checkbox"),
        DATE("date"),
        MONTH("month"),
        SELECT("select"),
        TEXT_AREA("textarea"),
        SUBMIT("submit"),
        PARAGRAPH("p"),
        GROUP("group"),
        ADDITIONAL_DATA("additionaldata"),
        EXPAND("expand");

        public final String code;

        Type(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public Type[] getValues() {
            return values();
        }

        public static Type parseOrThrow(String code) {
            return Enums.parseOrThrow(TEXT, code);
        }

        public static Type parse(String code) {
            return Enums.parse(TEXT, null, code);
        }

        public static Type parse(String code, Type defaultValue) {
            return Enums.parse(TEXT, defaultValue, code);
        }
    }

    public static abstract class Builder {

        public abstract Component create();
    }
}
