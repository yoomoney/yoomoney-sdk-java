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

import com.yandex.money.api.model.showcase.components.Parameter;
import com.yandex.money.api.util.ToStringBuilder;

import static com.yandex.money.api.util.Common.checkNotNull;

/**
 * Base class for all fields with internal state.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public abstract class ParameterControl extends Control implements Parameter {

    /**
     * Name.
     */
    public final String name;

    /**
     * Auto fill macro. The appropriate value should be inserted automatically.
     */
    public final AutoFill valueAutoFill;

    /**
     * Default value. May be {@code null}.
     */
    private String value;

    protected ParameterControl(Builder builder) {
        super(builder);
        name = checkNotNull(builder.name, "name");
        value = builder.value;
        valueAutoFill = builder.valueAutoFill;
    }

    /**
     * @return control's name
     */
    @Override
    public final String getName() {
        return name;
    }

    /**
     * @return default value. May be {@code null}
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value input value
     */
    @Override
    public void setValue(String value) {
        if (readonly) {
            throw new IllegalArgumentException("trying to set a value for readonly parameter '" + name + "'");
        }
        this.value = value;
        onValueSet(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ParameterControl that = (ParameterControl) o;

        return name.equals(that.name) && valueAutoFill == that.valueAutoFill &&
                !(value != null ? !value.equals(that.value) : that.value != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (valueAutoFill != null ? valueAutoFill.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public final boolean isValid() {
        return isValid(value);
    }

    /**
     * Checks passed argument across formal rules.
     *
     * @param value user's input.
     * @return {@code true} if value is valid and {@code false} otherwise.
     */
    public boolean isValid(String value) {
        return !required || (value != null && !value.isEmpty());
    }

    @Override
    protected ToStringBuilder getToStringBuilder() {
        return super.getToStringBuilder()
                .setName("ParameterControl")
                .append("name", name)
                .append("value", value)
                .append("valueAutoFill", valueAutoFill);
    }

    protected void onValueSet(String value) {
    }

    /**
     * Base builder of {@link ParameterControl}'s subclasses.
     */
    public static abstract class Builder extends Control.Builder {

        private String name;
        private String value;
        private AutoFill valueAutoFill;

        @Override
        public abstract ParameterControl create();

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setValue(String value) {
            this.value = value;
            return this;
        }

        public Builder setValueAutoFill(AutoFill valueAutoFill) {
            this.valueAutoFill = valueAutoFill;
            return this;
        }
    }
}
