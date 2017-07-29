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
import com.yandex.money.api.util.logging.Log;

/**
 * Email control.
 */
public class Email extends Text {

    @SuppressWarnings("WeakerAccess")
    protected Email(Builder builder) {
        super(builder);
    }

    /**
     * {@link Email} builder.
     */
    public static class Builder extends Text.Builder {

        public Builder() {
            super.setPattern(Patterns.EMAIL);
        }

        @Override
        public Email create() {
            return new Email(this);
        }

        @Override
        public Builder setMinLength(Integer minLength) {
            Log.w("email min length defined by predefined pattern");
            return this;
        }

        @Override
        public Builder setMaxLength(Integer maxLength) {
            Log.w("email max length defined by predefined pattern");
            return this;
        }

        @Override
        public Builder setPattern(String pattern) {
            Log.w("email has predefined pattern");
            return this;
        }

        @Override
        public Text.Builder setKeyboard(Keyboard keyboard) {
            Log.w("only email keyboards");
            return this;
        }
    }
}
