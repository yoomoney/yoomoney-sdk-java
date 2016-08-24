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

package com.yandex.money.api.typeadapters.model.showcase.uicontrol;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.model.showcase.components.Component;
import com.yandex.money.api.typeadapters.BaseTypeAdapter;
import com.yandex.money.api.typeadapters.model.showcase.ComponentsTypeProvider;

import java.lang.reflect.Type;

/**
 * Base class for {@link Component} adapters. All specific implementation should have a record in
 * {@link ComponentsTypeProvider} class.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public abstract class ComponentTypeAdapter<T extends Component, U extends Component.Builder>
        extends BaseTypeAdapter<T> {

    public static final String MEMBER_TYPE = "type";

    @Override
    public final T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return deserializeWithBuilder(json, context);
    }

    @Override
    public final JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject to = new JsonObject();
        to.addProperty(MEMBER_TYPE, ComponentsTypeProvider.getTypeFromClass(typeOfSrc));
        serialize(src, to, context);
        return to;
    }

    /**
     * Deserialization wrapper for {@param builder} filling.
     *
     * @param src     source JSON
     * @param builder destination builder
     * @param context proxy for deserialization of complicated (nested) hierarchies
     */
    protected abstract void deserialize(JsonObject src, U builder, JsonDeserializationContext context);

    /**
     * Serializes source object to {@link JsonObject}.
     *
     * @param src     source {@link Component}
     * @param to      destination JSON
     * @param context proxy for serialization of complicated (nested) hierarchies
     */
    protected abstract void serialize(T src, JsonObject to, JsonSerializationContext context);

    /**
     * @return empty component's builder. Needs to be implemented in leafs of class hierarchy.
     */
    protected abstract U createBuilderInstance();

    /**
     * Creates {@link Component} instance from builder.
     */
    protected abstract T createInstance(U builder);

    private T deserializeWithBuilder(JsonElement json, JsonDeserializationContext context) {
        U builder = createBuilderInstance();
        deserialize(json.getAsJsonObject(), builder, context);
        return createInstance(builder);
    }
}
