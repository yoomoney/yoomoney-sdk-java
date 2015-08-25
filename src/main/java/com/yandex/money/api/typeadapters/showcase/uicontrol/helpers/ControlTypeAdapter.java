package com.yandex.money.api.typeadapters.showcase.uicontrol.helpers;

import com.google.gson.JsonObject;
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
    protected void deserialize(JsonObject from, U builder) {
        builder.setAlert(getAsString(from, KEY_ALERT));
        builder.setHint(getAsString(from, KEY_HINT));
        builder.setLabel(getAsString(from, KEY_LABEL));
        builder.setReadonly(getAsBoolean(from, KEY_READONLY));
        builder.setRequired(getAsBoolean(from, KEY_REQUIRED));
    }

    @Override
    protected void serialize(T from, JsonObject to) {
        to.addProperty(KEY_ALERT, from.alert);
        to.addProperty(KEY_HINT, from.hint);
        to.addProperty(KEY_LABEL, from.label);
        to.addProperty(KEY_READONLY, from.readonly);
        to.addProperty(KEY_REQUIRED, from.required);
    }
}
