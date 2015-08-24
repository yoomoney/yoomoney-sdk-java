package com.yandex.money.api.typeadapters.showcase.uicontrol.helpers;

import com.google.gson.JsonObject;
import com.yandex.money.api.model.showcase.components.uicontrol.ParameterControl;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
abstract class ParameterControlTypeAdapter<T extends ParameterControl, U extends ParameterControl
        .Builder> extends ControlTypeAdapter<T, U> {

    private final String KEY_NAME = "name";
    private final String KEY_VALUE = "value";

    @Override
    protected void deserialize(JsonObject from, U builder) {
        builder.setName(from.get(KEY_NAME).getAsString());
        builder.setValue(from.get(KEY_VALUE).getAsString());
        super.deserialize(from, builder);
    }

    @Override
    protected void serialize(T from, JsonObject to) {
        to.addProperty(KEY_NAME, from.name);
        to.addProperty(KEY_VALUE, from.getValue());
        super.serialize(from, to);
    }
}
