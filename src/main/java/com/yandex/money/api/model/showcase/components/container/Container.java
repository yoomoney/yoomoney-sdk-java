package com.yandex.money.api.model.showcase.components.container;

import com.yandex.money.api.model.showcase.components.Component;

/**
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public abstract class Container extends Component {

    public final String label;

    protected Container(Builder builder) {
        label = builder.label;
    }

    public static abstract class Builder extends Component.Builder {

        private String label;

        public Builder setLabel(String label) {
            this.label = label;
            return this;
        }
    }
}
