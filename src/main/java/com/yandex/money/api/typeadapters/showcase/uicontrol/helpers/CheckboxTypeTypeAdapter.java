package com.yandex.money.api.typeadapters.showcase.uicontrol.helpers;

import com.google.gson.JsonObject;
import com.yandex.money.api.model.showcase.components.uicontrol.Checkbox;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class CheckboxTypeTypeAdapter extends ParameterControlTypeAdapter<Checkbox, Checkbox
        .Builder> {

    @Override
    protected void serialize(Checkbox from, JsonObject to) {
        to.addProperty("checked", from.checked);
        super.serialize(from, to);
    }

    @Override
    protected void deserializeToBuilder(JsonObject from, Checkbox.Builder builder) {
        builder.setChecked(from.get("checked").getAsBoolean());
        super.deserializeToBuilder(from, builder);
    }

    @Override
    protected Checkbox.Builder createBuilderInstance() {
        return new Checkbox.Builder();
    }

    @Override
    protected Checkbox createFromBuilder(Checkbox.Builder builder) {
        return builder.create();
    }

    @Override
    protected Class<Checkbox> getType() {
        return Checkbox.class;
    }
}
