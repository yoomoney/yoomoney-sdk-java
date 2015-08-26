package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.methods.JsonUtils;
import com.yandex.money.api.model.showcase.components.uicontrol.TextArea;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public abstract class TextAreaTypeAdapter<T extends TextArea, U extends TextArea.Builder>
        extends ParameterControlTypeAdapter<T, U> {

    public static final TextAreaTypeAdapter<TextArea, TextArea.Builder> INSTANCE =
            new TextAreaTypeAdapter<TextArea, TextArea.Builder>() {

                @Override
                protected TextArea.Builder createBuilderInstance() {
                    return new TextArea.Builder();
                }

                @Override
                protected TextArea createInstance(TextArea.Builder builder) {
                    return builder.create();
                }
            };

    private static final String KEY_MINLENGTH = "minlength";
    private static final String KEY_MAXLENGTH = "maxlength";

    @Override
    protected void deserialize(JsonObject from, U builder,
                               JsonDeserializationContext context) {
        builder.setMinLength(JsonUtils.getInt(from, KEY_MINLENGTH));
        builder.setMaxLength(JsonUtils.getInt(from, KEY_MAXLENGTH));
        super.deserialize(from, builder, context);
    }

    @Override
    protected void serialize(T from, JsonObject to, JsonSerializationContext context) {
        to.addProperty(KEY_MINLENGTH, from.minLength);
        to.addProperty(KEY_MAXLENGTH, from.maxLength);
        super.serialize(from, to, context);
    }
}
