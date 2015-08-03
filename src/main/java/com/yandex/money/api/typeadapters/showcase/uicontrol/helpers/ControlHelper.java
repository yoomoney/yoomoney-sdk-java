package com.yandex.money.api.typeadapters.showcase.uicontrol.helpers;

import com.google.gson.JsonObject;
import com.yandex.money.api.model.showcase.components.uicontrol.Control;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public abstract class ControlHelper<T extends Control, U extends Control.Builder> implements
        ComponentHelper<T, U> {

    @Override
    public void serialize(T from, JsonObject to) {
        to.addProperty("alert", from.alert);
        to.addProperty("hint", from.hint);
        to.addProperty("label", from.label);
        to.addProperty("readonly", from.readonly);
        to.addProperty("required", from.required);
    }

    @Override
    public void deserialize(JsonObject from, U to) {
        to.setAlert(from.get("alert").getAsString());
        to.setHint(from.get("hint").getAsString());
        to.setLabel(from.get("label").getAsString());
        to.setReadonly(from.get("readonly").getAsBoolean());
        to.setRequired(from.get("required").getAsBoolean());
    }
}
