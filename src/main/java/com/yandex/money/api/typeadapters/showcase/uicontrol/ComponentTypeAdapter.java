package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.yandex.money.api.model.showcase.Fee;
import com.yandex.money.api.model.showcase.components.Component;
import com.yandex.money.api.model.showcase.components.container.Group;
import com.yandex.money.api.model.showcase.components.container.Paragraph;
import com.yandex.money.api.model.showcase.components.uicontrol.Amount;
import com.yandex.money.api.model.showcase.components.uicontrol.Checkbox;
import com.yandex.money.api.model.showcase.components.uicontrol.Date;
import com.yandex.money.api.model.showcase.components.uicontrol.Email;
import com.yandex.money.api.model.showcase.components.uicontrol.Month;
import com.yandex.money.api.model.showcase.components.uicontrol.Number;
import com.yandex.money.api.model.showcase.components.uicontrol.Select;
import com.yandex.money.api.model.showcase.components.uicontrol.Submit;
import com.yandex.money.api.model.showcase.components.uicontrol.Tel;
import com.yandex.money.api.model.showcase.components.uicontrol.Text;
import com.yandex.money.api.model.showcase.components.uicontrol.TextArea;
import com.yandex.money.api.typeadapters.FeeTypeAdapter;
import com.yandex.money.api.typeadapters.TypeAdapter;
import com.yandex.money.api.typeadapters.showcase.ComponentsTypeProvider;
import com.yandex.money.api.typeadapters.showcase.container.GroupTypeAdapter;
import com.yandex.money.api.typeadapters.showcase.container.ParagraphTypeAdapter;

import java.lang.reflect.Type;

/**
 * Base class for {@link Component} adapters. All specific implementation should have a record in
 * {@link ComponentsTypeProvider} class.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public abstract class ComponentTypeAdapter<T extends Component, U extends Component.Builder>
        implements TypeAdapter<T>, JsonSerializer<T>, JsonDeserializer<T> {

    public static final String MEMBER_TYPE = "type";

    private static Gson gson;

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

    @Override
    public final T fromJson(String json) {
        return getGsonInstance().fromJson(json, getType());
    }

    @Override
    public final T fromJson(JsonElement element) {
        return getGsonInstance().fromJson(element, getType());
    }

    @Override
    public final String toJson(T value) {
        return getGsonInstance().toJson(value);
    }

    @Override
    public final JsonElement toJsonTree(T value) {
        return getGsonInstance().toJsonTree(value);
    }

    /**
     * @return appropriate {@link Component} type which type adapter belongs to
     */
    protected abstract Type getType();

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

    private static Gson getGsonInstance() {
        if (gson != null) {
            return gson;
        } else {
            gson = new GsonBuilder()
                    .registerTypeAdapter(Amount.class, AmountTypeAdapter.INSTANCE)
                    .registerTypeAdapter(Number.class, NumberTypeAdapter.INSTANCE)
                    .registerTypeAdapter(Month.class, MonthTypeAdapter.INSTANCE)
                    .registerTypeAdapter(Date.class, DateTypeAdapter.INSTANCE)
                    .registerTypeAdapter(Email.class, EmailTypeAdapter.INSTANCE)
                    .registerTypeAdapter(TextArea.class, TextAreaTypeAdapter.INSTANCE)
                    .registerTypeAdapter(Text.class, TextTypeAdapter.INSTANCE)
                    .registerTypeAdapter(Checkbox.class, CheckboxTypeAdapter.INSTANCE)
                    .registerTypeAdapter(Fee.class, FeeTypeAdapter.getInstance())
                    .registerTypeAdapter(Tel.class, TelTypeAdapter.INSTANCE)
                    .registerTypeAdapter(Select.class, SelectTypeAdapter.INSTANCE)
                    .registerTypeAdapter(Submit.class, SubmitTypeAdapter.INSTANCE)
                    .registerTypeAdapter(Group.class, GroupTypeAdapter.INSTANCE)
                    .registerTypeAdapter(Paragraph.class, ParagraphTypeAdapter.INSTANCE)
                    .create();
            return gson;
        }
    }

    private T deserializeWithBuilder(JsonElement json, JsonDeserializationContext context) {
        U builder = createBuilderInstance();
        deserialize(json.getAsJsonObject(), builder, context);
        return createInstance(builder);
    }
}
