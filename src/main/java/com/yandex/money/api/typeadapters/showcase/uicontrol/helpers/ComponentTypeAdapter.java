package com.yandex.money.api.typeadapters.showcase.uicontrol.helpers;

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
abstract class ComponentTypeAdapter<T extends Component, U extends Component.Builder> extends
        BaseTypeAdapter<T> {

    @Override
    public final T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext
            context) throws
            JsonParseException {
        return proxyBuilder(json);
    }

    @Override
    public final JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject to = new JsonObject();
        serialize(src, to);
        return to;
    }

    protected abstract U createBuilderInstance();

    protected abstract void deserializeToBuilder(JsonObject from, U builder);

    protected abstract T createFromBuilder(U builder);

    protected abstract void serialize(T from, JsonObject to);

    private T proxyBuilder(JsonElement json) {
        U builder = createBuilderInstance();
        deserializeToBuilder(json.getAsJsonObject(), builder);
        return createFromBuilder(builder);
    }
}
