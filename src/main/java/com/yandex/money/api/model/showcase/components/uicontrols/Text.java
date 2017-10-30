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

package com.yandex.money.api.model.showcase.components.uicontrols;


import com.yandex.money.api.util.Enums;

/**
 * Text field. Specializes {@link TextArea} with optional keyboard layout and pattern.
 *
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public class Text extends TextArea {

    /**
     * Constraint represented as regular expression. May be {@code null}.
     * <p/>
     * {@see java.util.regex.Pattern}
     */
    public final String pattern;

    /**
     * Convenient keyboard suggestion. May be {@code null}.
     */
    public final Keyboard keyboard;

    protected Text(Builder builder) {
        super(builder);
        pattern = builder.pattern;
        keyboard = builder.keyboard;
    }

    @Override
    public boolean isValid(String value) {
        return super.isValid(value) && (value == null || value.isEmpty() ||
                (pattern == null || value.matches(pattern)) && !value.contains("\n"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Text text = (Text) o;

        return !(pattern != null ? !pattern.equals(text.pattern) : text.pattern != null) &&
                keyboard == text.keyboard;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (pattern != null ? pattern.hashCode() : 0);
        result = 31 * result + (keyboard != null ? keyboard.hashCode() : 0);
        return result;
    }

    /**
     * Keyboard type. The {@link Keyboard#NUMBER} means that field value can be filled with
     * number keyboard.
     */
    public enum Keyboard implements Enums.WithCode<Keyboard> {

        NUMBER("number");

        public final String code;

        Keyboard(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public Keyboard[] getValues() {
            return values();
        }

        public static Keyboard parse(String code) {
            return Enums.parse(NUMBER, code);
        }
    }

    /**
     * {@link Text} builder.
     */
    public static class Builder extends TextArea.Builder {

        String pattern;
        Keyboard keyboard;

        @Override
        public Text create() {
            return new Text(this);
        }

        public Builder setPattern(String pattern) {
            this.pattern = pattern;
            return this;
        }

        public Builder setKeyboard(Keyboard keyboard) {
            this.keyboard = keyboard;
            return this;
        }
    }
}
