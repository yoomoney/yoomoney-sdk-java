package com.yandex.money.api.model.showcase.components.uicontrol;

/**
 * On/off control.
 *
 * @author Aleksandr Ershov (asershov@yamoney.ru)
 */
public final class Checkbox extends ParameterControl {

    /**
     * Initial state. Default is {@code false}.
     */
    public final boolean checked;

    private Checkbox(Builder builder) {
        super(builder);
        checked = builder.checked;
    }

    @Override
    public boolean isValid(String value) {
        return super.isValid(value) &&
                (value == null || value.isEmpty() || "true".equals(value) || "false".equals(value));
    }

    /**
     * {@link Checkbox} builder.
     */
    public static final class Builder extends ParameterControl.Builder {

        private boolean checked = false;

        @Override
        public Checkbox create() {
            return new Checkbox(this);
        }

        public Builder setChecked(Boolean checked) {
            if (checked != null) {
                this.checked = checked;
            }
            return this;
        }
    }
}