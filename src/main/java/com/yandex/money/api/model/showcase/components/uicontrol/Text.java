package com.yandex.money.api.model.showcase.components.uicontrol;


/**
 * Text field. It's like {@link TextArea} but may have optional keyboard suggestion and pattern.
 *
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public class Text extends TextArea {

    /**
     * User's input constraint represented as regular expression. May be {@code null}.
     * {@see https://developer.mozilla
     * .org/en-US/docs/Web/JavaScript/Reference/Global_Objects/RegExp}.
     */
    public final String pattern;

    /**
     * Convenient keyboard suggestion. May be {@code null}.
     */
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

    /**
     * {@link Text} builder.
     */
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

    /**
     * Keyboard type. The {@link Keyboard#NUMBER} means that field value can be filled with
     * number keyboard.
     */
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
