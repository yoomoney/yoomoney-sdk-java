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

import com.yandex.money.api.util.Patterns;
import com.yandex.money.api.util.ToStringBuilder;

/**
 * Telephone number control.
 *
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public class Tel extends Text {

    protected Tel(Builder builder) {
        super(builder);
    }

    @Override
    protected ToStringBuilder getToStringBuilder() {
        return super.getToStringBuilder().setName("Tel");
    }

    /**
     * {@link Tel builder}.
     */
    public static class Builder extends Text.Builder {

        public Builder() {
            super.setPattern(Patterns.PHONE);
        }

        @Override
        public Tel create() {
            return new Tel(this);
        }

        @Override
        public Builder setMinLength(Integer minLength) {
            throw new UnsupportedOperationException("tel min length defined by predefined pattern");
        }

        @Override
        public Builder setMaxLength(Integer maxLength) {
            throw new UnsupportedOperationException("tel max length defined by predefined pattern");
        }

        @Override
        public Builder setPattern(String pattern) {
            throw new UnsupportedOperationException("tel has predefined pattern");
        }

        @Override
        public Text.Builder setKeyboard(Keyboard keyboard) {
            throw new UnsupportedOperationException("only tel keyboards");
        }
    }
}
