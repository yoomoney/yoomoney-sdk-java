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
abstract class BaseTextAreaTypeAdapter<T extends TextArea, U extends TextArea.Builder>
        extends ParameterControlTypeAdapter<T, U> {

    private static final String MEMBER_MINLENGTH = "minlength";
    private static final String MEMBER_MAXLENGTH = "maxlength";

    @Override
    protected void deserialize(JsonObject src, U builder,
                               JsonDeserializationContext context) {
        builder.setMinLength(JsonUtils.getInt(src, MEMBER_MINLENGTH));
        builder.setMaxLength(JsonUtils.getInt(src, MEMBER_MAXLENGTH));
        super.deserialize(src, builder, context);
    }

    @Override
    protected void serialize(T src, JsonObject to, JsonSerializationContext context) {
        to.addProperty(MEMBER_MINLENGTH, src.minLength);
        to.addProperty(MEMBER_MAXLENGTH, src.maxLength);
        super.serialize(src, to, context);
    }
}
