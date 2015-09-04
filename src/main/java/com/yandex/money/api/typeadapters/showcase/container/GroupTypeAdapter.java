package com.yandex.money.api.typeadapters.showcase.container;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.model.showcase.components.Component;
import com.yandex.money.api.model.showcase.components.container.Group;
import com.yandex.money.api.typeadapters.showcase.ComponentsTypeProvider;

import java.lang.reflect.Type;

/**
 * Type serializer for {@link Group} component container.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class GroupTypeAdapter extends ContainerTypeAdapter<Component, Group, Group.Builder> {

    public static final GroupTypeAdapter INSTANCE = new GroupTypeAdapter();

    private GroupTypeAdapter() {
    }

    @Override
    protected Group.Builder createBuilderInstance() {
        return new Group.Builder();
    }

    @Override
    protected Group createInstance(Group.Builder builder) {
        return builder.create();
    }

    @Override
    protected JsonElement serializeItem(Component src, JsonSerializationContext context) {
        return context.serialize(src, src.getClass());
    }

    @Override
    protected Component deserializeItem(JsonElement src, JsonDeserializationContext context) {
        return context.deserialize(src, ComponentsTypeProvider.getJsonComponentType(src));
    }

    @Override
    protected Type getType() {
        return Group.class;
    }
}
