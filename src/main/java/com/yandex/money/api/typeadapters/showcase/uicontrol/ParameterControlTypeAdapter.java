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

    private final String MEMBER_NAME = "name";
    private final String MEMBER_VALUE = "value";
    private final String MEMBER_AUTOFILL = "value_autofill";

    @Override
    protected void deserialize(JsonObject src, U builder, JsonDeserializationContext context) {
        builder.setName(JsonUtils.getString(src, MEMBER_NAME));
        builder.setValue(JsonUtils.getString(src, MEMBER_VALUE));

        if (src.has(MEMBER_AUTOFILL)) {
            builder.setValueAutoFill(Parameter.AutoFill.parse(src.get(MEMBER_AUTOFILL)
                    .getAsString()));
        }
        super.deserialize(src, builder, context);
    }

    @Override
    protected void serialize(T src, JsonObject to, JsonSerializationContext context) {
        to.addProperty(MEMBER_NAME, src.name);
        to.addProperty(MEMBER_VALUE, src.getValue());
        if (src.valueAutoFill != null) {
            to.addProperty(MEMBER_AUTOFILL, src.valueAutoFill.code);
        }
        super.serialize(src, to, context);
    }
}
