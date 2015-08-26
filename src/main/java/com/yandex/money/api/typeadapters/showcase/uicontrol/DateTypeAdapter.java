package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.methods.JsonUtils;
import com.yandex.money.api.model.showcase.components.uicontrol.Date;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class DateTypeAdapter extends ParameterControlTypeAdapter<Date, Date.Builder> {

    private static final String KEY_MIN = "min";
    private static final String KEY_MAX = "max";


    @Override
    protected void deserialize(JsonObject from, Date.Builder builder, JsonDeserializationContext
            context) {
        builder.setMin(JsonUtils.getDateTime(from, KEY_MIN));
        builder.setMax(JsonUtils.getDateTime(from, KEY_MAX));
        super.deserialize(from, builder, context);
    }

    @Override
    protected void serialize(Date from, JsonObject to, JsonSerializationContext context) {
        super.serialize(from, to, context);
    }

    @Override
    protected Date.Builder createBuilderInstance() {
        return new Date.Builder();
    }

    @Override
    protected Date createInstance(Date.Builder builder) {
        return builder.create();
    }
}
