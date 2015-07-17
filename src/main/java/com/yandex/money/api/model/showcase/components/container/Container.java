package com.yandex.money.api.model.showcase.components.container;

import com.yandex.money.api.model.showcase.components.Component;

/**
 * Base class for all component containers.
 *
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public abstract class Container extends Component {

    /**
     * Textural representation of contained items.
     */
    public final String label;

    protected Container(Builder builder) {
        label = builder.label;
    }

    /**
     * Base class for all component builders.
     */
    public static abstract class Builder extends Component.Builder {

        private String label;

        public Builder setLabel(String label) {
            this.label = label;
            return this;
        }
    }
}
