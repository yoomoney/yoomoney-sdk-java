package com.yandex.money.api.model.showcase.components.container;

import com.yandex.money.api.model.showcase.components.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A {@link Container} is special component that can hold other components (items).
 *
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public abstract class Container<T> extends Component {

    /**
     * Items.
     */
    public final List<T> items;

    /**
     * Label. Can be {@code null}.
     */
    public final String label;

    protected Container(Builder<T> builder) {
        label = builder.label;
        items = Collections.unmodifiableList(builder.components);
    }

    /**
     * Base class for all component builders.
     */
    public static abstract class Builder<T> extends Component.Builder {

        private final List<T> components;

        private String label;

        public Builder() {
            this.components = new ArrayList<>();
        }

        public Builder setLabel(String label) {
            this.label = label;
            return this;
        }

        public Builder addItem(T component) {
            if (component == null) {
                throw new NullPointerException("component is null");
            }
            components.add(component);
            return this;
        }
    }
}
