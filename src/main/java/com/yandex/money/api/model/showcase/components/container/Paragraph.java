package com.yandex.money.api.model.showcase.components.container;

import com.yandex.money.api.model.showcase.components.TextBlock;

/**
 * Sequence of text blocks.
 *
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public final class Paragraph extends Container<TextBlock> {

    private Paragraph(Builder builder) {
        super(builder);
    }

    /**
     * {@link Paragraph} builder.
     */
    public static final class Builder extends Container.Builder<TextBlock> {

        @Override
        public Paragraph create() {
            return new Paragraph(this);
        }
    }
}
