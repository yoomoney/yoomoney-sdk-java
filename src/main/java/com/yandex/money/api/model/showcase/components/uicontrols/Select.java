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

import com.yandex.money.api.model.showcase.components.containers.Group;
import com.yandex.money.api.util.Enums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.yandex.money.api.util.Common.checkNotNull;

/**
 * Control for selecting amongst a set of options.
 *
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public class Select extends ParameterControl {

    /**
     * Options.
     */
    public final List<Option> options;

    /**
     * Values.
     */
    public final List<String> values;

    /**
     * Recommended representation style. Default is {@code null}.
     */
    public final Style style;

    private Option selectedOption;

    @SuppressWarnings("WeakerAccess")
    protected Select(Builder builder) {
        super(builder);
        options = Collections.unmodifiableList(checkNotNull(builder.options, "options"));
        values = Collections.unmodifiableList(getValues(options));
        style = builder.style;
    }

    @Override
    public boolean isValid(String value) {
        return super.isValid(value) && (value == null || value.isEmpty() ||
                values.contains(value) && (selectedOption == null || selectedOption.isValid()));
    }

    /**
     * @return default option. May be {@code null}.
     */
    public Option getSelectedOption() {
        return selectedOption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Select select = (Select) o;

        return options.equals(select.options) && values.equals(select.values) &&
                style == select.style && !(selectedOption != null ?
                !selectedOption.equals(select.selectedOption) : select.selectedOption != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + options.hashCode();
        result = 31 * result + values.hashCode();
        result = 31 * result + (style != null ? style.hashCode() : 0);
        result = 31 * result + (selectedOption != null ? selectedOption.hashCode() : 0);
        return result;
    }

    @Override
    protected void onValueSet(String value) {
        int index = values.indexOf(value);
        if (index < 0) {
            if (value == null) {
                selectedOption = null;
            } else {
                setValue(null);
            }
        } else {
            selectedOption = options.get(index);
        }
    }

    private static List<String> getValues(List<Option> options) {
        List<String> values = new ArrayList<>(options.size());
        for (Option option : options) {
            values.add(option.value);
        }
        return values;
    }

    /**
     * Style of {@link Select} representation.
     */
    public enum Style implements Enums.WithCode<Style> {

        /**
         * Options should be arranged as group of radio buttons.
         */
        RADIO_GROUP("RadioGroup"),

        /**
         * Options should be arranged as spinner.
         */
        SPINNER("Spinner");

        public final String code;

        Style(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public Style[] getValues() {
            return values();
        }

        public static Style parse(String code) {
            return Enums.parse(SPINNER, SPINNER, code);
        }
    }

    /**
     * Label-value container.
     */
    public static class Option {

        /**
         * Label.
         */
        public final String label;

        /**
         * Value.
         */
        public final String value;

        /**
         * Group of elements which have to be visible when {@link Option} is selected. May be
         * {@code null}.
         */
        public final Group group;

        /**
         * Constructor.
         *
         * @param label textural representation.
         * @param value actual value.
         */
        public Option(String label, String value, Group group) {
            this.label = checkNotNull(label, "label");
            this.value = checkNotNull(value, "value");
            this.group = group;
        }

        /**
         * Validates current control state.
         *
         * @return if the {@link Option} is valid or not.
         */
        public boolean isValid() {
            return group == null || group.isValid();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Option option = (Option) o;

            return label.equals(option.label) && value.equals(option.value) &&
                    !(group != null ? !group.equals(option.group) : option.group != null);
        }

        @Override
        public int hashCode() {
            int result = label.hashCode();
            result = 31 * result + value.hashCode();
            result = 31 * result + (group != null ? group.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Option{" +
                    "label='" + label + '\'' +
                    ", value='" + value + '\'' +
                    ", group=" + group +
                    '}';
        }
    }

    /**
     * {@link Select} builder.
     */
    public static class Builder extends ParameterControl.Builder {

        final List<Option> options = new ArrayList<>();

        Style style;

        @Override
        public Select create() {
            return new Select(this);
        }

        @SuppressWarnings("UnusedReturnValue")
        public Builder setStyle(Style style) {
            this.style = style;
            return this;
        }

        public Builder addOption(Option option) {
            options.add(checkNotNull(option, "option"));
            return this;
        }
    }
}
