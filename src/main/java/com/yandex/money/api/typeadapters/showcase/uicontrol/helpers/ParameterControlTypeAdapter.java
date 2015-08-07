package com.yandex.money.api.typeadapters.showcase.uicontrol.helpers;

import com.google.gson.JsonObject;
import com.yandex.money.api.model.showcase.components.uicontrol.ParameterControl;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
abstract class ParameterControlTypeAdapter<T extends ParameterControl, U extends ParameterControl
        .Builder> extends ControlTypeAdapter<T, U> {

    @Override
    protected void serialize(T from, JsonObject to) {
        to.addProperty("name", from.name);
        to.addProperty("value", from.getValue());
        super.serialize(from, to);
    }

    @Override
    protected void deserializeToBuilder(JsonObject from, U builder) {
        builder.setName(from.get("name").getAsString());
        builder.setValue(from.get("value").getAsString());
        super.deserializeToBuilder(from, builder);
    }
}
