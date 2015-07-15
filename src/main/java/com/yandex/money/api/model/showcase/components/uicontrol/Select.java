package com.yandex.money.api.model.showcase.components.uicontrol;

import com.yandex.money.api.model.showcase.components.container.Group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public final class Select extends ParameterControl {

    public final List<Option> options;
    public final List<String> values;
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

    public Option getSelectedOption() {
        return selectedOption;
    }

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

    public enum Style {
        RADIO_GROUP("RadioGroup"),
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

    public static final class Option {

        public final String label;
        public final String value;

        public Group group;

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

        public boolean isValid() {
            return group == null || group.isValid();
        }
    }

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
