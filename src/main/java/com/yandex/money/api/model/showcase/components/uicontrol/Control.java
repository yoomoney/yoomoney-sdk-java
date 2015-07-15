package com.yandex.money.api.model.showcase.components.uicontrol;

import com.yandex.money.api.model.showcase.components.Component;

/**
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public abstract class Control extends Component {

    public final String hint;
    public final String label;
    public final String alert;
    public final boolean required;
    public final boolean readonly;
    // TODO decide if 'alert', 'required' and 'readonly' fields should be in ParameterControl class

    protected Control(Builder builder) {
        hint = builder.hint;
        label = builder.label;
        alert = builder.alert;
        required = builder.required;
        readonly = builder.readonly;
    }

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
