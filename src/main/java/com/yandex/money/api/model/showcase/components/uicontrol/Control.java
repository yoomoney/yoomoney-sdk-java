package com.yandex.money.api.model.showcase.components.uicontrol;

import com.yandex.money.api.model.showcase.components.Component;

/**
 * Base class for all components that allows user's input.
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

    /**
     * Control builder.
     */
    protected Control(Builder builder) {
        hint = builder.hint;
        label = builder.label;
        alert = builder.alert;
        required = builder.required;
        readonly = builder.readonly;
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
