package com.yandex.money.api.model.showcase.components.uicontrol;

import com.yandex.money.api.model.showcase.components.Component;
import com.yandex.money.api.utils.ToStringBuilder;

/**
 * Base class for all components that allow user's input.
 * <p/>
 * TODO: think about if 'alert', 'required' and 'readonly' fields should be in ParameterControl
 * class.
 *
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public abstract class Control extends Component {

    /**
     * Hint. May be {@code null}.
     */
    public final String hint;

    /**
     * Label. May be {@code null}.
     */
    public final String label;

    /**
     * Text which explains why the user's input is erroneous. May be {@code null}.
     */
    public final String alert;

    /**
     * User's input is required if the value is {@code true}. Default is {@code true}.
     */
    public final boolean required;

    /**
     * Control is read-only if the value is {@code true}. Default is {@code false}.
     */
    public final boolean readonly;

    protected Control(Builder builder) {
        hint = builder.hint;
        label = builder.label;
        alert = builder.alert;
        required = builder.required;
        readonly = builder.readonly;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Control control = (Control) o;

        return required == control.required && readonly == control.readonly &&
                !(hint != null ? !hint.equals(control.hint) : control.hint != null) &&
                !(label != null ? !label.equals(control.label) : control.label != null) &&
                !(alert != null ? !alert.equals(control.alert) : control.alert != null);

    }

    @Override
    public int hashCode() {
        int result = hint != null ? hint.hashCode() : 0;
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (alert != null ? alert.hashCode() : 0);
        result = 31 * result + (required ? 1 : 0);
        result = 31 * result + (readonly ? 1 : 0);
        return result;
    }

    @Override
    protected ToStringBuilder getToStringBuilder() {
        return new ToStringBuilder("Control")
                .append("hint", hint)
                .append("label", label)
                .append("alert", alert)
                .append("required", required)
                .append("readonly", readonly);
    }

    /**
     * {@link Control} builder.
     */
    public static abstract class Builder extends Component.Builder {

        private String hint;
        private String label;
        private String alert;
        private boolean required;
        private boolean readonly;

        public Builder() {
            this.required = true;
            this.readonly = false;
        }

        public abstract Control create();

        public Builder setHint(String hint) {
            this.hint = hint;
            return this;
        }

        public Builder setLabel(String label) {
            this.label = label;
            return this;
        }

        public Builder setAlert(String alert) {
            this.alert = alert;
            return this;
        }

        public Builder setRequired(boolean required) {
            this.required = required;
            return this;
        }

        public Builder setReadonly(boolean readonly) {
            this.readonly = readonly;
            return this;
        }
    }
}
