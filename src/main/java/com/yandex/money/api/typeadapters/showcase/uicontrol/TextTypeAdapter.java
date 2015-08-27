package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.methods.JsonUtils;
import com.yandex.money.api.model.showcase.components.uicontrol.Text;

/**
 * Base type adapter for subclasses of {@link Text} component.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public abstract class TextTypeAdapter<T extends Text, U extends Text.Builder>
        extends TextAreaTypeAdapter<T, U> {

    /**
     * Type adapter for {@link Text} component.
     */
    public static final TextTypeAdapter<Text, Text.Builder> INSTANCE = new TextTypeAdapter<Text,
            Text.Builder>() {

        @Override
        protected Text.Builder createBuilderInstance() {
            return new Text.Builder();
        }

        @Override
        protected Text createInstance(Text.Builder builder) {
            return builder.create();
        }
    };

    private static final String KEY_PATTERN = "pattern";
    private static final String KEY_KEYBOARD_SUGGEST = "keyboard_suggest";

    @Override
    protected void deserialize(JsonObject src, U builder, JsonDeserializationContext context) {
        builder.setPattern(JsonUtils.getString(src, KEY_PATTERN));
        builder.setKeyboard(Text.Keyboard.parse(JsonUtils.getString(src, KEY_KEYBOARD_SUGGEST)));
        super.deserialize(src, builder, context);
    }

    @Override
    protected void serialize(T src, JsonObject to, JsonSerializationContext context) {
        to.addProperty(KEY_PATTERN, src.pattern);
        to.addProperty(KEY_KEYBOARD_SUGGEST, src.keyboard == null ? null : src.keyboard.code);
        super.serialize(src, to, context);
    }
}
