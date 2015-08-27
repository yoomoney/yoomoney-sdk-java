package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.methods.JsonUtils;
import com.yandex.money.api.model.showcase.components.Parameter;
import com.yandex.money.api.model.showcase.components.uicontrol.ParameterControl;

/**
 * Base type adapter for components implementing {@link ParameterControl} interface.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
abstract class ParameterControlTypeAdapter<T extends ParameterControl, U extends ParameterControl
        .Builder> extends ControlTypeAdapter<T, U> {

    private final String KEY_NAME = "name";
    private final String KEY_VALUE = "value";
    private final String KEY_AUTOFILL = "value_autofill";

    @Override
    protected void deserialize(JsonObject src, U builder, JsonDeserializationContext context) {
        builder.setName(JsonUtils.getString(src, KEY_NAME));
        builder.setValue(JsonUtils.getString(src, KEY_VALUE));

        if (src.has(KEY_AUTOFILL)) {
            builder.setValueAutoFill(Parameter.AutoFill.parse(src.get(KEY_AUTOFILL)
                    .getAsString()));
        }
        super.deserialize(src, builder, context);
    }

    @Override
    protected void serialize(T src, JsonObject to, JsonSerializationContext context) {
        to.addProperty(KEY_NAME, src.name);
        to.addProperty(KEY_VALUE, src.getValue());
        if (src.valueAutoFill != null) {
            to.addProperty(KEY_AUTOFILL, src.valueAutoFill.code);
        }
        super.serialize(src, to, context);
    }
}
