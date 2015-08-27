package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.yandex.money.api.model.showcase.components.Component;
import com.yandex.money.api.typeadapters.showcase.ComponentsTypeProvider;

import java.lang.reflect.Type;

/**
 * Base class for {@link Component} adapters.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public abstract class ComponentTypeAdapter<T extends Component, U extends Component.Builder>
        implements JsonSerializer<T>, JsonDeserializer<T> {

    @Override
    public final T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext
            context) throws
            JsonParseException {
        return deserializeWithBuilder(json, context);
    }

    @Override
    public final JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject to = new JsonObject();
        to.addProperty("type", ComponentsTypeProvider.getTypeFromClass(typeOfSrc));
        serialize(src, to, context);
        return to;
    }

    protected abstract void deserialize(JsonObject from, U builder,
                                        JsonDeserializationContext context);

    protected abstract void serialize(T from, JsonObject to, JsonSerializationContext context);

    protected abstract U createBuilderInstance();

    protected abstract T createInstance(U builder);

    private T deserializeWithBuilder(JsonElement json, JsonDeserializationContext context) {
        U builder = createBuilderInstance();
        deserialize(json.getAsJsonObject(), builder, context);
        return createInstance(builder);
    }
}
