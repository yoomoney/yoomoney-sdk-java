package com.yandex.money.api.model.showcase.components.uicontrol;

import com.yandex.money.api.model.showcase.components.container.Group;
import com.yandex.money.api.utils.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Control for selecting amongst a set of options.
 *
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public final class Select extends ParameterControl {

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

    private Select(Builder builder) {
        super(builder);
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
     * Returns default option. May be {@code null}.
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
    protected ToStringBuilder getToStringBuilder() {
        return new ToStringBuilder("Select")
                .append("options", options)
                .append("values", values)
                .append("style", style)
                .append("selectedOption", selectedOption)
                .append(super.getToStringBuilder());
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

        Style(String code) {
            this.code = code;
        }

        public static Style parse(String code) {
            for (Style type : values()) {
                if (type.code.equalsIgnoreCase(code)) {
                    return type;
                }
            }
            return SPINNER;
        }

        @Override
        public String toString() {
            return "Style{" +
                    "code='" + code + '\'' +
                    "} " + super.toString();
        }
    }

    /**
     * Label-value container.
     */
    public static final class Option {

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
         * Validates current control state.
         *
         * @return if the {@link Option} is valid or not.
         */
        public boolean isValid() {
            return group == null || group.isValid();
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
    public static final class Builder extends ParameterControl.Builder {

        private List<Option> options = new ArrayList<>();
        private Style style;

        @Override
        public Select create() {
            setType(Type.SELECT);
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