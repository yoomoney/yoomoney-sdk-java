package com.yandex.money.api.model.showcase.components.uicontrol;

import com.yandex.money.api.utils.Patterns;

/**
 * Email control.
 * <p/>
 * TODO: refactor inheritance.
 *
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public final class Email extends Text {

    private Email(Builder builder) {
        super(builder);
    }

    /**
     * {@link Email} builder.
     */
    public static final class Builder extends Text.Builder {

        public Builder() {
            super.setPattern(Patterns.EMAIL);
        }

        @Override
        public Email create() {
            return new Email(this);
        }

        @Override
        public Builder setMinLength(Integer minLength) {
            throw new UnsupportedOperationException(
                    "email min length defined by predefined pattern");
        }

        @Override
        public Builder setMaxLength(Integer maxLength) {
            throw new UnsupportedOperationException(
                    "email max length defined by predefined pattern");
        }

        @Override
        public Builder setPattern(String pattern) {
            throw new UnsupportedOperationException("email has predefined pattern");
        }

        @Override
        public Text.Builder setKeyboard(Keyboard keyboard) {
            throw new UnsupportedOperationException("only email keyboards");
        }
    }
}
