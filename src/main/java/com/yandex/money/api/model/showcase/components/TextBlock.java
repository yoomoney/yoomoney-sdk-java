package com.yandex.money.api.model.showcase.components;

/**
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public class TextBlock {

    public final String label;

    public TextBlock(String label) {
        this.label = label;
    }

    public static final class WithLink extends TextBlock {

        public final String link;

        public WithLink(String label, String link) {
            super(label);
            this.link = link;
        }
    }
}
