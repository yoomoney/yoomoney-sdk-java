package com.yandex.money.api.typeadapters.showcase.uicontrol.helpers;

import com.google.gson.JsonObject;
import com.yandex.money.api.model.showcase.components.Parameter;
import com.yandex.money.api.model.showcase.components.uicontrol.ParameterControl;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class ParameterControlHelper<T extends ParameterControl, U extends
        ParameterControl.Builder> extends ControlHelper<T, U> {

    @Override
    public void serialize(T from, JsonObject to) {
        super.serialize(from, to);
        to.addProperty("name", from.name);
        to.addProperty("value", from.getValue());
        if (from.valueAutoFill != null) {
            to.addProperty("value_autofill", from.valueAutoFill.code);
        }
    }

    @Override
    public void deserialize(JsonObject from, U to) {
        super.deserialize(from, to);
        to.setName(from.get("name").getAsString());
        to.setValue(from.get("value").getAsString());
        to.setValueAutoFill(Parameter.AutoFill.parse(from.get("value_autofill").getAsString()));
    }
}
