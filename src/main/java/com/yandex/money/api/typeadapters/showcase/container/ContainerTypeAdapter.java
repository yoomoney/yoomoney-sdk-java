package com.yandex.money.api.typeadapters.showcase.container;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.methods.JsonUtils;
import com.yandex.money.api.model.showcase.components.container.Container;
import com.yandex.money.api.typeadapters.showcase.uicontrol.ComponentTypeAdapter;

/**
 * Base type adapter for subclasses of {@link Container} component.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public abstract class ContainerTypeAdapter<T, U extends Container<T>, K extends Container
        .Builder<T>> extends ComponentTypeAdapter<U, K> {

    private static final String MEMBER_LABEL = "label";
    private static final String MEMBER_ITEMS = "items";

    @Override
    protected final void deserialize(JsonObject src, K builder, JsonDeserializationContext
            context) {
        for (JsonElement item : src.getAsJsonArray(MEMBER_ITEMS)) {
            builder.addItem(deserializeItem(item, context));
        }
        builder.setLabel(JsonUtils.getString(src, MEMBER_LABEL));
    }

    @Override
    protected final void serialize(U src, JsonObject to, JsonSerializationContext context) {
        JsonArray array = new JsonArray();

        for (T item : src.items) {
            array.add(serializeItem(item, context));
        }
        to.addProperty(MEMBER_LABEL, src.label);
        to.add(MEMBER_ITEMS, array);
    }

    /**
     * Serializes {@link Container}'s item.
     *
     * @param src     {@link Container}'s item
     * @param context serialization context
     * @return JSON element representing a {@param src}
     */
    protected abstract JsonElement serializeItem(T src, JsonSerializationContext context);

    /**
     * Deserializes {@link Container}'s item.
     *
     * @param src     source JSON
     * @param context deserialization context
     * @return {@link Container}'s item
     */
    protected abstract T deserializeItem(JsonElement src, JsonDeserializationContext context);

}
