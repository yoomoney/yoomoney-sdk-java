package com.yandex.money.api.typeadapters.showcase.uicontrol.helpers;

import com.google.gson.JsonObject;
import com.yandex.money.api.model.showcase.components.Parameter;
import com.yandex.money.api.model.showcase.components.uicontrol.ParameterControl;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
abstract class ParameterControlTypeAdapter<T extends ParameterControl, U extends ParameterControl
        .Builder> extends ControlTypeAdapter<T, U> {

    private final String KEY_NAME = "name";
    private final String KEY_VALUE = "value";
    private final String KEY_AUTOFILL = "value_autofill";

    @Override
    protected void deserialize(JsonObject from, U builder) {
        builder.setName(getAsString(from, KEY_NAME));
        builder.setValue(getAsString(from, KEY_VALUE));

        if (from.has(KEY_AUTOFILL)) {
            builder.setValueAutoFill(Parameter.AutoFill.parse(from.get(KEY_AUTOFILL)
                    .getAsString()));
        }
        super.deserialize(from, builder);
    }

    @Override
    protected void serialize(T from, JsonObject to) {
        to.addProperty(KEY_NAME, from.name);
        to.addProperty(KEY_VALUE, from.getValue());
        if (from.valueAutoFill != null) {
            to.addProperty(KEY_AUTOFILL, from.valueAutoFill.code);
        }
        super.serialize(from, to);
    }
}
