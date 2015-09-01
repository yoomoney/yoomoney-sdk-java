package com.yandex.money.api.model.showcase.components.uicontrol;

import com.yandex.money.api.model.showcase.components.Parameter;
import com.yandex.money.api.utils.ToStringBuilder;

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
        if (builder.name == null) {
            throw new NullPointerException("name is null");
        }
        name = builder.name;
        value = builder.value;
        valueAutoFill = builder.valueAutoFill;
    }

    /**
     * TODO: remove method field's public modifier.
     *
     * @return control's name
     */
    @Override
    public final String getName() {
        return name;
    }

    /**
     * @return default value. May be {@code null}.
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * Sets value.
     * <p/>
     * TODO: isValid() call?
     *
     * @param value input value.
     */
    @Override
    public void setValue(String value) {
        if (readonly) {
            throw new IllegalArgumentException("trying to set value for readonly parameter '" +
                    value + "'");
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

    /**
     * Validates control state.
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

    @Override
    protected ToStringBuilder getToStringBuilder() {
        return super.getToStringBuilder()
                .setName("ParameterControl")
                .append("name", name)
                .append("value", value)
                .append("valueAutoFill", valueAutoFill);
    }

    /**
     * TODO: is this method required?
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
