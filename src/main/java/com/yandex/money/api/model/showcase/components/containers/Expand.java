package com.yandex.money.api.model.showcase.components.containers;

import com.yandex.money.api.model.showcase.components.Component;

import java.util.Objects;

import static com.yandex.money.api.util.Common.checkNotNull;

/**
 * A {@link Expand} is implementation of a {@link Component} that can expand and contain only {@link Component}
 * instances.
 */
public class Expand extends Container<Component> {

    /**
     * Label shown when container is minimized.
     */
    public final String labelMinimized;

    /**
     * Label shown when container is expanded.
     */
    public final String labelExpanded;

    @SuppressWarnings("WeakerAccess")
    protected Expand(Builder builder) {
        super(builder);
        labelMinimized = checkNotNull(builder.labelMinimized, "labelMinimized");
        labelExpanded = checkNotNull(builder.labelExpanded, "labelExpanded");
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

        Expand expand = (Expand) o;

        if (!Objects.equals(labelMinimized, expand.labelMinimized))
            return false;
        return Objects.equals(labelExpanded, expand.labelExpanded);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (labelMinimized != null ? labelMinimized.hashCode() : 0);
        result = 31 * result + (labelExpanded != null ? labelExpanded.hashCode() : 0);
        return result;
    }

    /**
     * {@link Expand} builder.
     */
    public static class Builder extends Container.Builder<Component> {

        String labelMinimized;
        String labelExpanded;

        @Override
        public Expand create() {
            return new Expand(this);
        }

        @SuppressWarnings("UnusedReturnValue")
        public Expand.Builder setLabelMinimized(String labelMinimized) {
            this.labelMinimized = labelMinimized;
            return this;
        }

        @SuppressWarnings("UnusedReturnValue")
        public Expand.Builder setLabelExpanded(String labelExpanded) {
            this.labelExpanded = labelExpanded;
            return this;
        }
    }
}
