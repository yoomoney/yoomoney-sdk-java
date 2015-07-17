package com.yandex.money.api.model.showcase.components.uicontrol;

import com.yandex.money.api.model.showcase.components.container.Group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Group of selectable options.
 *
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public final class Select extends ParameterControl {

    /**
     * Held options.
     */
    public final List<Option> options;

    /**
     * TODO: remove this field.
     */
    public final List<String> values;

    /**
     * Recommended representation style.
     */
    public final Style style;

    private Option selectedOption;

    private Select(Builder builder) {
        super(builder);
        if (builder.options == null) {
            throw new NullPointerException("options is null");
        }
        options = Collections.unmodifiableList(builder.options);
        values = Collections.unmodifiableList(getValues(options));
        style = builder.style;
    }

    @Override
    public boolean isValid(String value) {
        return super.isValid(value) && (value == null || value.isEmpty() ||
                values.contains(value) && (selectedOption == null || selectedOption.isValid()));
    }

    /**
     * @return current selected option.
     */
    public Option getSelectedOption() {
        return selectedOption;
    }

    /**
     * TODO: is this method required?
     */
    @Override
    protected void onValueSet(String value) {
        selectedOption = options.get(values.indexOf(value));
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
    public enum Style {

        /**
         * Options should be arranged as group of radio buttons.
         */
        RADIO_GROUP("RadioGroup"),

        /**
         * Options should be arranged as spinner.
         */
        SPINNER("Spinner");

        public final String code;

        public static Style parse(String typeName) {
            for (Style type : values()) {
                if (type.code.equalsIgnoreCase(typeName)) {
                    return type;
                }
            }
            return SPINNER;
        }

        Style(String code) {
            this.code = code;
        }
    }

    /**
     * Label-value container.
     */
    public static final class Option {

        /**
         * Textural representation.
         */
        public final String label;

        /**
         * Actual value.
         */
        public final String value;

        /**
         * Group of elements which have to be visible when {@link Option} is selected. May be
         * {@code null}.
         * TODO: constructor?
         */
        public Group group;

        /**
         * Constructor.
         *
         * @param label textural representation.
         * @param value actual value.
         */
        public Option(String label, String value) {
            if (label == null) {
                throw new NullPointerException("label is null");
            }
            if (value == null) {
                throw new NullPointerException("value is null");
            }
            this.label = label;
            this.value = value;
        }

        /**
         * Validates current input's state.
         * @return if the {@link Option} is valid or not.
         */
        public boolean isValid() {
            return group == null || group.isValid();
        }
    }

    /**
     * {@link Select} builder.
     */
    public static final class Builder extends ParameterControl.Builder {

        private List<Option> options = new ArrayList<>();
        private Style style;

        @Override
        public Select create() {
            return new Select(this);
        }

        public Builder setStyle(Style style) {
            this.style = style;
            return this;
        }

        public Builder addOption(Option option) {
            if (option == null) {
                throw new NullPointerException("option is null");
            }
            options.add(option);
            return this;
        }
    }
}
