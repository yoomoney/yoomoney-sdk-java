package com.yandex.money.api.model.showcase.components.uicontrol;

import com.yandex.money.api.model.showcase.components.Parameter;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public abstract class ParameterControl extends Control implements Parameter {

    public final String name;
    public final AutoFill valueAutoFill;

    private String value;

    protected ParameterControl(Builder builder) {
        super(builder);
        if (builder.name == null) {
            throw new NullPointerException("name is null");
        }
        name = builder.name;
        value = builder.value;
        valueAutoFill = builder.valueAutoFill;
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final void setValue(String value) {
        if (readonly) {
            throw new IllegalArgumentException("trying to set value for readonly parameter '" +
                    value + "'");
        }
        this.value = value;
        onValueSet(value);
    }

    @Override
    public final String getValue() {
        return value;
    }

    public final boolean isValid() {
        return isValid(value);
    }

    public boolean isValid(String value) {
        return !required || (value != null && !value.isEmpty());
    }

    protected void onValueSet(String value) {
    }

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
