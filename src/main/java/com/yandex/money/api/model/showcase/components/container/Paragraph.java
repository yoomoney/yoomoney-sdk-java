package com.yandex.money.api.model.showcase.components.container;

import com.yandex.money.api.model.showcase.components.TextBlock;
import com.yandex.money.api.utils.ToStringBuilder;

/**
 * Sequence of text blocks.
 *
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public final class Paragraph extends Container<TextBlock> {

    private Paragraph(Builder builder) {
        super(builder);
    }

    @Override
    protected ToStringBuilder getToStringBuilder() {
        return super.getToStringBuilder().setName("Paragraph");
    }

    /**
     * {@link Paragraph} builder.
     */
    public static final class Builder extends Container.Builder<TextBlock> {

        @Override
        public Paragraph create() {
            setType(Type.PARAGRAPH);
            return new Paragraph(this);
        }
    }
}
