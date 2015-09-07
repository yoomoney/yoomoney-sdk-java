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
abstract class BaseTextTypeAdapter<T extends Text, U extends Text.Builder>
        extends TextAreaTypeAdapter<T, U> {

    private static final String MEMBER_PATTERN = "pattern";
    private static final String MEMBER_MEMBERBOARD_SUGGEST = "keyboard_suggest";

    @Override
    protected void deserialize(JsonObject src, U builder, JsonDeserializationContext context) {
        builder.setPattern(JsonUtils.getString(src, MEMBER_PATTERN));
        builder.setKeyboard(Text.Keyboard.parse(JsonUtils.getString(src,
                MEMBER_MEMBERBOARD_SUGGEST)));
        super.deserialize(src, builder, context);
    }

    @Override
    protected void serialize(T src, JsonObject to, JsonSerializationContext context) {
        to.addProperty(MEMBER_PATTERN, src.pattern);
        to.addProperty(MEMBER_MEMBERBOARD_SUGGEST, src.keyboard == null ? null : src.keyboard.code);
        super.serialize(src, to, context);
    }
}

