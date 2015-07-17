package com.yandex.money.api.model.showcase.components.uicontrol;

import com.yandex.money.api.model.showcase.components.Parameter;

/**
 * Base class for all fields with internal state.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public abstract class ParameterControl extends Control implements Parameter {

    /**
     * Field name.
     */
    public final String name;

    /**
     * Auto fill macro. The appropriate value should be substituted automatically.
     */
    public final AutoFill valueAutoFill;

    /**
     * Field value.
     */
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

    /**
     * Returns name.
     * <p/>
     * TODO: remove method field's public modifier.
     *
     * @return name
     */
    @Override
    public final String getName() {
        return name;
    }

    /**
     * Sets value.
     *
     * TODO: isValid() call?
     * @param value input value.
     */
    @Override
    public final void setValue(String value) {
        if (readonly) {
            throw new IllegalArgumentException("trying to set value for readonly parameter '" +
                    value + "'");
        }
        this.value = value;
        onValueSet(value);
    }

    /**
     * Returns current value.
     *
     * @return field value.
     */
    @Override
    public final String getValue() {
        return value;
    }

    /**
     * Validates input's state.
     *
     * @return {@code true} if instance is valid and {@code false} otherwise.
     */
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

    /**
     * TODO: is this method required?
     * @param value
     */
    protected void onValueSet(String value) {
    }

    /**
     * Base builder of {@link ParameterControl}'s subclasses.
     * TODO: protected?
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
