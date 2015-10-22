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
import com.yandex.money.api.utils.Enums;
import com.yandex.money.api.utils.ToStringBuilder;

import static com.yandex.money.api.utils.Common.checkNotNull;

/**
 * A {@link Group} is implementation of a {@link Component} that can contain only {@link Component}
 * instances.
 *
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public class Group extends Container<Component> {

    /**
     * {@link Layout}.
     */
    public final Layout layout;

    private Group(Builder builder) {
        super(builder);
        layout = builder.layout;
    }

    /**
     * Validates contained components across constraints.
     *
     * @return {@code true} if group is valid and {@code false} otherwise.
     */
    @Override
    public boolean isValid() {
        for (Component component : items) {
            if (!component.isValid()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Group group = (Group) o;

        return layout == group.layout;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + layout.hashCode();
        return result;
    }

    @Override
    protected ToStringBuilder getToStringBuilder() {
        return super.getToStringBuilder()
                .setName("Group")
                .append("layout", layout);
    }

    /**
     * Possible options that specifies arrangement of contained {@link Component}s.
     */
    public enum Layout implements Enums.WithCode<Layout> {

        /**
         * Vertical arrangement.
         */
        VERTICAL("VBox"),

        /**
         * Horizontal arrangement.
         */
        HORIZONTAL("HBox");

        public final String code;

        Layout(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public Layout[] getValues() {
            return values();
        }

        public static Layout parse(String code) {
            return Enums.parse(VERTICAL, VERTICAL, code);
        }
    }

    /**
     * {@link Group} builder.
     */
    public static class Builder extends Container.Builder<Component> {

        private Layout layout = Layout.VERTICAL;

        @Override
        public Group create() {
            return new Group(this);
        }

        public Builder setLayout(Layout layout) {
            checkNotNull(layout, "layout");
            this.layout = layout;
            return this;
        }
    }
}
