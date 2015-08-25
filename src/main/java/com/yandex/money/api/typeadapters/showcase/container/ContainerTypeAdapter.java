package com.yandex.money.api.typeadapters.showcase.container;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.model.showcase.components.container.Container;
import com.yandex.money.api.typeadapters.showcase.uicontrol.ComponentTypeAdapter;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public abstract class ContainerTypeAdapter<T, U extends Container<T>, K extends Container
        .Builder<T>> extends ComponentTypeAdapter<U, K> {

    @Override
    protected void deserialize(JsonObject from, K builder, JsonDeserializationContext context) {
        for (JsonElement item : from.getAsJsonArray("items")) {
            builder.addItem(deserializeItem(item, context));
        }
        builder.setLabel(getAsString(from, "label"));
    }

    @Override
    protected void serialize(U from, JsonObject to, JsonSerializationContext context) {
        JsonArray array = new JsonArray();

        for (T item : from.items) {
            array.add(serializeItem(item, context));
        }
        to.addProperty("label", from.label);
        to.add("items", array);
    }

    protected abstract JsonElement serializeItem(T item, JsonSerializationContext context);

    protected abstract T deserializeItem(JsonElement json, JsonDeserializationContext context);

}
