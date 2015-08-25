package com.yandex.money.api.model.showcase.components.uicontrol;

import com.yandex.money.api.utils.Patterns;
import com.yandex.money.api.utils.ToStringBuilder;

/**
 * Telephone number control.
 * <p/>
 * TODO: maybe this class should extend ParameterControl?
 *
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public final class Tel extends Text {

    private Tel(Builder builder) {
        super(builder);
    }

    @Override
    protected ToStringBuilder getToStringBuilder() {
        return super.getToStringBuilder().setName("Tel");
    }

    /**
     * {@link Tel builder}.
     */
    public static final class Builder extends Text.Builder {

        public Builder() {
            setType(Type.TEL);
            super.setPattern(Patterns.PHONE);
        }

        @Override
        public Tel create() {
            return new Tel(this);
        }

        @Override
        public Builder setMinLength(Integer minLength) {
            throw new UnsupportedOperationException("tel min length defined by predefined pattern");
        }

        @Override
        public Builder setMaxLength(Integer maxLength) {
            throw new UnsupportedOperationException("tel max length defined by predefined pattern");
        }

        @Override
        public Builder setPattern(String pattern) {
            throw new UnsupportedOperationException("tel has predefined pattern");
        }

        @Override
        public Text.Builder setKeyboard(Keyboard keyboard) {
            throw new UnsupportedOperationException("only tel keyboards");
        }
    }
}
