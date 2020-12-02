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

package com.yoo.money.api.model.showcase.components.uicontrols;

import com.yoo.money.api.util.Patterns;
import com.yoo.money.api.util.logging.Log;

/**
 * Telephone number control.
 */
public class Tel extends Text {

    @SuppressWarnings("WeakerAccess")
    protected Tel(Builder builder) {
        super(builder);
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
            Log.w("tel min length defined by predefined pattern");
            return this;
        }

        @Override
        public Builder setMaxLength(Integer maxLength) {
            Log.w("tel max length defined by predefined pattern");
            return this;
        }

        @Override
        public Builder setPattern(String pattern) {
            Log.w("tel has predefined pattern");
            return this;
        }

        @Override
        public Text.Builder setKeyboard(Keyboard keyboard) {
            Log.w("only tel keyboards");
            return this;
        }
    }
}
