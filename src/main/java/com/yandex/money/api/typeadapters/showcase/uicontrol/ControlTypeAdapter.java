package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.methods.JsonUtils;
import com.yandex.money.api.model.showcase.components.uicontrol.Control;

/**
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
    protected void deserialize(JsonObject from, U builder, JsonDeserializationContext context) {
        builder.setAlert(JsonUtils.getString(from, KEY_ALERT));
        builder.setHint(JsonUtils.getString(from, KEY_HINT));
        builder.setLabel(JsonUtils.getString(from, KEY_LABEL));
        if (from.has(KEY_READONLY)) {
            builder.setReadonly(from.get(KEY_READONLY).getAsBoolean());
        }
        if (from.has(KEY_REQUIRED)) {
            builder.setRequired(from.get(KEY_REQUIRED).getAsBoolean());
        }
    }

    @Override
    protected void serialize(T from, JsonObject to, JsonSerializationContext context) {
        to.addProperty(KEY_ALERT, from.alert);
        to.addProperty(KEY_HINT, from.hint);
        to.addProperty(KEY_LABEL, from.label);
        if (from.readonly) {
            to.addProperty(KEY_READONLY, true);
        }

        if (!from.required) {
            to.addProperty(KEY_REQUIRED, false);
        }
    }
}
