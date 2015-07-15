package com.yandex.money.api.model.showcase.components.container;

import com.yandex.money.api.model.showcase.components.Component;
import com.yandex.money.api.model.showcase.components.uicontrol.ParameterControl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public final class Group extends Container {

    public final List<Component> items;
    public final Layout layout;

    private Group(Builder builder) {
        super(builder);
        layout = builder.layout;
        items = Collections.unmodifiableList(builder.items);
    }

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

    public enum Layout {
        VERTICAL("VBox"),
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
