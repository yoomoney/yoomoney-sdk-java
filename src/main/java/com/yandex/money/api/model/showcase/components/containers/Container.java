/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 NBCO Yandex.Money LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.yandex.money.api.model.showcase.components.containers;

import com.yandex.money.api.model.showcase.components.Component;
import com.yandex.money.api.util.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.yandex.money.api.util.Common.checkNotNull;

/**
 * A generic {@link Container} object is special component that can contain other items
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
        items = Collections.unmodifiableList(checkNotNull(builder.items, "items"));
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

    @Override
    protected ToStringBuilder getToStringBuilder() {
        return new ToStringBuilder("Container")
                .append("label", label)
                .append("items", items);
    }

    /**
     * Base class for all component builders.
     */
    public static abstract class Builder<T> extends Component.Builder {

        final List<T> items = new ArrayList<>();

        String label;

        public Builder setLabel(String label) {
            this.label = label;
            return this;
        }

        public Builder addItem(T item) {
            items.add(item);
            return this;
        }

        public Builder addItems(List<T> items) {
            this.items.addAll(items);
            return this;
        }
    }
}
