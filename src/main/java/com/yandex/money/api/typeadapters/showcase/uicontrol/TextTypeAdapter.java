package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.methods.JsonUtils;
import com.yandex.money.api.model.showcase.components.uicontrol.Text;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public abstract class TextTypeAdapter<T extends Text, U extends Text.Builder>
        extends TextAreaTypeAdapter<T, U> {

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
    protected void deserialize(JsonObject from, U builder, JsonDeserializationContext context) {
        builder.setPattern(JsonUtils.getString(from, KEY_PATTERN));
        builder.setKeyboard(Text.Keyboard.parse(JsonUtils.getString(from, KEY_KEYBOARD_SUGGEST)));
        super.deserialize(from, builder, context);
    }

    @Override
    protected void serialize(T from, JsonObject to, JsonSerializationContext context) {
        to.addProperty(KEY_PATTERN, from.pattern);
        to.addProperty(KEY_KEYBOARD_SUGGEST, from.keyboard == null ? null : from.keyboard.code);
        super.serialize(from, to, context);
    }
}
