package com.yandex.money.api.model.showcase.components.container;

import com.yandex.money.api.model.showcase.components.Component;
import com.yandex.money.api.model.showcase.components.uicontrol.ParameterControl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Holder of collection of {@link Component}s.
 *
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public final class Group extends Container {

    /**
     * Collection of components.
     */
    public final List<Component> items;

    /**
     * Arrangement.
     */
    public final Layout layout;

    private Group(Builder builder) {
        super(builder);
        layout = builder.layout;
        items = Collections.unmodifiableList(builder.items);
    }

    /**
     * Validates items across specified rules.
     * <p/>
     * TODO: refactor inheritance.
     *
     * @return {@code true} if group is valid and {@code false} otherwise.
     */
    public boolean isValid() {
        for (Component component : items) {
            boolean valid = true;
            if (component instanceof ParameterControl) {
                valid = ((ParameterControl) component).isValid();
            } else if (component instanceof Group) {
                valid = ((Group) component).isValid();
            }
            if (!valid) {
                return false;
            }
        }
        return true;
    }

    /**
     * Layout that specified arrangement of contained {@link Component}s.
     */
    public enum Layout {

        /**
         * Vertical arrangement.
         */
        VERTICAL("VBox"),

        /**
         * Horizontal arrangement.
         */
        HORIZONTAL("HBox");

        public final String code;

        public static Layout parse(String typeName) {
            for (Layout type : values()) {
                if (type.code.equalsIgnoreCase(typeName)) {
                    return type;
                }
            }
            return VERTICAL;
        }

        Layout(String code) {
            this.code = code;
        }
    }

    /**
     * Builder for {@link Group}.
     */
    public static final class Builder extends Container.Builder {

        private List<Component> items = new ArrayList<>();
        private Layout layout = Layout.VERTICAL;

        public Builder() {
            super();
        }

        @Override
        public Group create() {
            return new Group(this);
        }

        public Builder setLayout(Layout layout) {
            if (layout == null) {
                throw new NullPointerException("layout is null");
            }
            this.layout = layout;
            return this;
        }

        public Builder addItem(Component component) {
            if (component == null) {
                throw new NullPointerException("component is null");
            }
            items.add(component);
            return this;
        }
    }
}
