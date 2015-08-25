package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.model.showcase.components.uicontrol.Checkbox;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class CheckboxTypeTypeAdapter extends ParameterControlTypeAdapter<Checkbox, Checkbox
        .Builder> {

    @Override
    protected void serialize(Checkbox from, JsonObject to, JsonSerializationContext context) {
        to.addProperty("checked", from.checked);
        super.serialize(from, to, context);
    }

    @Override
    protected void deserialize(JsonObject from, Checkbox.Builder builder,
                               JsonDeserializationContext context) {
        builder.setChecked(from.get("checked").getAsBoolean());
        super.deserialize(from, builder, context);
    }

    @Override
    protected Checkbox.Builder createBuilderInstance() {
        return new Checkbox.Builder();
    }

    @Override
    protected Checkbox createInstance(Checkbox.Builder builder) {
        return builder.create();
    }

    @Override
    protected Class<Checkbox> getType() {
        return Checkbox.class;
    }
}
