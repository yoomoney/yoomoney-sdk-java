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

package com.yandex.money.api.typeadapters.model.showcase.container;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.model.showcase.components.Undefined;
import com.yandex.money.api.model.showcase.components.containers.Container;
import com.yandex.money.api.typeadapters.model.showcase.uicontrol.ComponentTypeAdapter;

import static com.yandex.money.api.typeadapters.JsonUtils.getString;

/**
 * Base type adapter for subclasses of {@link Container} component.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
abstract class ContainerTypeAdapter<T, U extends Container<T>, K extends Container.Builder<T>>
        extends ComponentTypeAdapter<U, K> {

    private static final String MEMBER_ITEMS = "items";
    private static final String MEMBER_LABEL = "label";

    @Override
    protected void deserialize(JsonObject src, K builder, JsonDeserializationContext context) {
        for (JsonElement item : src.getAsJsonArray(MEMBER_ITEMS)) {
            T deserializedItem = deserializeItem(item, context);
            if (!(deserializedItem instanceof Undefined)) {
                builder.addItem(deserializedItem);
            }
        }
        builder.setLabel(getString(src, MEMBER_LABEL));
    }

    @Override
    protected void serialize(U src, JsonObject to, JsonSerializationContext context) {
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
