package com.yandex.money.api.model.showcase.components.uicontrol;

import com.yandex.money.api.utils.ToStringBuilder;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Checkbox checkbox = (Checkbox) o;

        return checked == checkbox.checked;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (checked ? 1 : 0);
        return result;
    }

    @Override
    protected ToStringBuilder getToStringBuilder() {
        return new ToStringBuilder("Checkbox")
                .append("checked", checked)
                .append(super.getToStringBuilder());
    }

    /**
     * {@link Checkbox} builder.
     */
    public static final class Builder extends ParameterControl.Builder {

        private boolean checked = false;

        @Override
        public Checkbox create() {
            setType(Type.CHECKBOX);
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