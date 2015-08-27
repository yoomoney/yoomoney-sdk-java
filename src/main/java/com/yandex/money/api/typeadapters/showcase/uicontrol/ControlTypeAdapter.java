package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.methods.JsonUtils;
import com.yandex.money.api.model.showcase.components.uicontrol.Control;

/**
 * Base type adapter for components implementing {@link Control} interface.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
abstract class ControlTypeAdapter<T extends Control, U extends Control.Builder> extends
        ComponentTypeAdapter<T, U> {

    private static final String KEY_ALERT = "alert";
    private static final String KEY_HINT = "hint";
    private static final String KEY_LABEL = "label";
    private static final String KEY_READONLY = "readonly";
    private static final String KEY_REQUIRED = "required";

    @Override
    protected void deserialize(JsonObject src, U builder, JsonDeserializationContext context) {
        builder.setAlert(JsonUtils.getString(src, KEY_ALERT));
        builder.setHint(JsonUtils.getString(src, KEY_HINT));
        builder.setLabel(JsonUtils.getString(src, KEY_LABEL));
        if (src.has(KEY_READONLY)) {
            builder.setReadonly(src.get(KEY_READONLY).getAsBoolean());
        }
        if (src.has(KEY_REQUIRED)) {
            builder.setRequired(src.get(KEY_REQUIRED).getAsBoolean());
        }
    }

    @Override
    protected void serialize(T src, JsonObject to, JsonSerializationContext context) {
        to.addProperty(KEY_ALERT, src.alert);
        to.addProperty(KEY_HINT, src.hint);
        to.addProperty(KEY_LABEL, src.label);
        if (src.readonly) {
            to.addProperty(KEY_READONLY, true);
        }

        if (!src.required) {
            to.addProperty(KEY_REQUIRED, false);
        }
    }
}
