package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.model.showcase.components.Component;
import com.yandex.money.api.typeadapters.BaseTypeAdapter;

import java.lang.reflect.Type;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public abstract class ComponentTypeAdapter<T extends Component, U extends Component.Builder> extends
        BaseTypeAdapter<T> {

    @Override
    public final T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext
            context) throws
            JsonParseException {
        return deserializeWithBuilder(json, context);
    }

    @Override
    public final JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject to = new JsonObject();
        serialize(src, to, context);
        return to;
    }

    protected abstract U createBuilderInstance();

    protected abstract void deserialize(JsonObject from, U builder,
                                        JsonDeserializationContext context);

    protected abstract T createInstance(U builder);

    protected abstract void serialize(T from, JsonObject to, JsonSerializationContext context);

    protected final String getAsString(JsonObject json, String key) {
        return json.has(key) ? json.get(key).getAsString() : null;
    }

    protected final Boolean getAsBoolean(JsonObject json, String key) {
        return json.has(key) ? json.get(key).getAsBoolean() : null;
    }

    private T deserializeWithBuilder(JsonElement json, JsonDeserializationContext context) {
        U builder = createBuilderInstance();
        deserialize(json.getAsJsonObject(), builder, context);
        return createInstance(builder);
    }
}
