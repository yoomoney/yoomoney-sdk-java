package com.yandex.money.api.model.showcase.components.container;

import com.yandex.money.api.model.showcase.components.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A generic {@link Container} object is special component that can contain other components
 * (items).
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Container<?> container = (Container<?>) o;

        return items.equals(container.items) &&
                !(label != null ? !label.equals(container.label) : container.label != null);

    }

    @Override
    public int hashCode() {
        int result = items.hashCode();
        result = 31 * result + (label != null ? label.hashCode() : 0);
        return result;
    }

    /**
     * Base class for all component builders.
     */
    public static abstract class Builder<T> extends Component.Builder {

        private final List<T> components = new ArrayList<>();

        private String label;

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
