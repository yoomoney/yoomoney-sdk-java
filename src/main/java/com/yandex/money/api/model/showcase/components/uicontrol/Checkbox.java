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
    public boolean checked;

    private Checkbox(Builder builder) {
        super(builder);
        checked = builder.checked;
    }

    @Override
    public boolean isValid(String value) {
        return !required || checked;
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
    public String getValue() {
        return checked ? super.getValue() : null;
    }

    @Override
    public void setValue(String value) {
        throw new UnsupportedOperationException("call setChecked(boolean) instead");
    }

    /**
     * Sets {@code checked} value. Should be used instead of {@link #setValue(String)}.
     *
     * @param checked {@code true} if checked.
     */
    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    protected ToStringBuilder getToStringBuilder() {
        return super.getToStringBuilder()
                .setName("Checkbox")
                .append("checked", checked);
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