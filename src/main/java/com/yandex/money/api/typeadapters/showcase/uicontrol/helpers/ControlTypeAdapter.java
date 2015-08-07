package com.yandex.money.api.typeadapters.showcase.uicontrol.helpers;

import com.google.gson.JsonObject;
import com.yandex.money.api.model.showcase.components.uicontrol.Control;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
abstract class ControlTypeAdapter<T extends Control, U extends Control.Builder> extends
        ComponentTypeAdapter<T, U> {

    @Override
    protected void deserializeToBuilder(JsonObject from, U builder) {
        builder.setAlert(from.get("alert").getAsString());
    }

    @Override
    protected void serialize(T from, JsonObject to) {
        to.addProperty("alert", from.alert);
    }
}
