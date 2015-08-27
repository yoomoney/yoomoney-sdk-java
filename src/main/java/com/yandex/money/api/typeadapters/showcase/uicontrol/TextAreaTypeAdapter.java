package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.methods.JsonUtils;
import com.yandex.money.api.model.showcase.components.uicontrol.TextArea;

/**
 * Base type adapter for subclasses of {@link TextArea} component.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public abstract class TextAreaTypeAdapter<T extends TextArea, U extends TextArea.Builder>
        extends ParameterControlTypeAdapter<T, U> {

    /**
     * Type adapter for {@link TextArea} component.
     */
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
    protected void deserialize(JsonObject src, U builder,
                               JsonDeserializationContext context) {
        builder.setMinLength(JsonUtils.getInt(src, KEY_MINLENGTH));
        builder.setMaxLength(JsonUtils.getInt(src, KEY_MAXLENGTH));
        super.deserialize(src, builder, context);
    }

    @Override
    protected void serialize(T src, JsonObject to, JsonSerializationContext context) {
        to.addProperty(KEY_MINLENGTH, src.minLength);
        to.addProperty(KEY_MAXLENGTH, src.maxLength);
        super.serialize(src, to, context);
    }
}
