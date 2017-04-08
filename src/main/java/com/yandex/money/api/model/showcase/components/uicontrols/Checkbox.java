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

/**
 * On/off control.
 *
 * @author Aleksandr Ershov (asershov@yamoney.ru)
 */
public class Checkbox extends ParameterControl {

    /**
     * Initial state. Default is {@code false}.
     */
    public boolean checked;

    protected Checkbox(Builder builder) {
        super(builder);
        checked = builder.checked;
    }

    @Override
    public boolean isValid(String value) {
        return !required || checked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Checkbox checkbox = (Checkbox) o;

        return checked == checkbox.checked;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (checked ? 1 : 0);
        return result;
    }

    @Override
    public String getValue() {
        return checked ? super.getValue() : null;
    }

    @Override
    public void setValue(String value) {
        throw new UnsupportedOperationException("call setChecked(boolean) instead");
    }

    /**
     * {@link Checkbox} builder.
     */
    public static class Builder extends ParameterControl.Builder {

        boolean checked = false;

        @Override
        public Checkbox create() {
            return new Checkbox(this);
        }

        public Builder setChecked(Boolean checked) {
            if (checked != null) {
                this.checked = checked;
            }
            return this;
        }
    }
}