package com.yandex.money.api.model.showcase.components.uicontrol;


/**
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public class Text extends TextArea {

    public final String pattern;
    public final Keyboard keyboard;

    protected Text(Builder builder) {
        super(builder);
        pattern = builder.pattern;
        keyboard = builder.keyboard;
    }

    @Override
    public boolean isValid(String value) {
        return super.isValid(value) && (value == null || value.isEmpty() ||
                (pattern == null || value.matches(pattern)) && !value.contains("\n"));
    }

    public static class Builder extends TextArea.Builder {

        private String pattern;
        private Keyboard keyboard;

        @Override
        public Text create() {
            return new Text(this);
        }

        public Builder setPattern(String pattern) {
            this.pattern = pattern;
            return this;
        }

        public Builder setKeyboard(Keyboard keyboard) {
            this.keyboard = keyboard;
            return this;
        }
    }

    public enum Keyboard {
        NUMBER("number");

        public final String code;

        Keyboard(String code) {
            this.code = code;
        }

        public static Keyboard parse(String code) {
            for (Keyboard keyboard : values()) {
                if (keyboard.code.equals(code)) {
                    return keyboard;
                }
            }
            return null;
        }
    }
}
