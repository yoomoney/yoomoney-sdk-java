package com.yandex.money.api.model.showcase.components.container;

import com.yandex.money.api.model.showcase.components.TextBlock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public final class Paragraph extends Container {

    public List<TextBlock> items;

    private Paragraph(Builder builder) {
        super(builder);
        items = Collections.unmodifiableList(builder.items);
    }

    public static final class Builder extends Container.Builder {

        private List<TextBlock> items = new ArrayList<>();

        public Builder() {
            super();
        }

        @Override
        public Paragraph create() {
            return new Paragraph(this);
        }

        public Builder addItem(TextBlock item) {
            if (items == null) {
                throw new NullPointerException("items is null");
            }
            items.add(item);
            return this;
        }
    }
}
