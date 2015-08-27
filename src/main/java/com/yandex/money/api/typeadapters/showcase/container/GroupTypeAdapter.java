package com.yandex.money.api.typeadapters.showcase.container;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.model.showcase.components.Component;
import com.yandex.money.api.model.showcase.components.container.Group;
import com.yandex.money.api.typeadapters.showcase.ComponentTypeAdapterFactory;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class GroupTypeAdapter extends ContainerTypeAdapter<Component, Group, Group.Builder> {

    @Override
    protected Group.Builder createBuilderInstance() {
        return new Group.Builder();
    }

    @Override
    protected Group createInstance(Group.Builder builder) {
        return builder.create();
    }

    @Override
    protected JsonElement serializeItem(Component item, JsonSerializationContext context) {
        return context.serialize(item, item.getClass());
    }

    @Override
    protected Component deserializeItem(JsonElement json, JsonDeserializationContext context) {
        return context.deserialize(json, ComponentTypeAdapterFactory.getJsonComponentType(json));
    }
}
