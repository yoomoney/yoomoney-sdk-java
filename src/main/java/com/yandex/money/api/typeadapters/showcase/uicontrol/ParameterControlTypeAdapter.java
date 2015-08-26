package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.methods.JsonUtils;
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
    protected void deserialize(JsonObject from, U builder, JsonDeserializationContext context) {
        builder.setName(JsonUtils.getString(from, KEY_NAME));
        builder.setValue(JsonUtils.getString(from, KEY_VALUE));

        if (from.has(KEY_AUTOFILL)) {
            builder.setValueAutoFill(Parameter.AutoFill.parse(from.get(KEY_AUTOFILL)
                    .getAsString()));
        }
        super.deserialize(from, builder, context);
    }

    @Override
    protected void serialize(T from, JsonObject to, JsonSerializationContext context) {
        to.addProperty(KEY_NAME, from.name);
        to.addProperty(KEY_VALUE, from.getValue());
        if (from.valueAutoFill != null) {
            to.addProperty(KEY_AUTOFILL, from.valueAutoFill.code);
        }
        super.serialize(from, to, context);
    }
}
