package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.model.showcase.components.Component;
import com.yandex.money.api.typeadapters.BaseTypeAdapter;
import com.yandex.money.api.typeadapters.showcase.ComponentsTypeProvider;

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
    public final T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext
            context) throws
            JsonParseException {
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
    protected abstract void deserialize(JsonObject src, U builder,
                                        JsonDeserializationContext context);

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
