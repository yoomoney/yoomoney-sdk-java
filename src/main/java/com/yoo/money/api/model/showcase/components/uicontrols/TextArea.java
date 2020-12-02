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

/**
 * Text field for arbitrary user's input.
 *
 * @author Aleksandr Ershov (support@yoomoney.ru)
 */
public class TextArea extends ParameterControl {

    /**
     * Minimal text length.
     */
    public final Integer minLength;

    /**
     * Maximum text length.
     */
    public final Integer maxLength;

    protected TextArea(Builder builder) {
        super(builder);
        if (builder.minLength != null && builder.maxLength != null && builder.minLength > builder.maxLength) {
            throw new IllegalArgumentException("minLength > maxLength");
        }
        minLength = builder.minLength;
        maxLength = builder.maxLength;
    }

    @Override
    public boolean isValid(String value) {
        return super.isValid(value) && (value == null || value.isEmpty() ||
                (minLength == null || value.length() >= minLength) &&
                        (maxLength == null || value.length() <= maxLength));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TextArea textArea = (TextArea) o;

        return !(minLength != null ? !minLength.equals(textArea.minLength) :
                textArea.minLength != null) && !(maxLength != null ?
                !maxLength.equals(textArea.maxLength) : textArea.maxLength != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (minLength != null ? minLength.hashCode() : 0);
        result = 31 * result + (maxLength != null ? maxLength.hashCode() : 0);
        return result;
    }

    /**
     * {@link TextArea} builder.
     */
    public static class Builder extends ParameterControl.Builder {

        Integer minLength;
        Integer maxLength;

        @Override
        public TextArea create() {
            return new TextArea(this);
        }

        public Builder setMinLength(Integer minLength) {
            this.minLength = minLength;
            return this;
        }

        public Builder setMaxLength(Integer maxLength) {
            this.maxLength = maxLength;
            return this;
        }
    }
}
