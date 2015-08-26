package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.methods.JsonUtils;
import com.yandex.money.api.model.showcase.components.uicontrol.Date;

import org.joda.time.format.DateTimeFormatter;


/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public abstract class DateTypeAdapter<T extends Date, U extends Date.Builder> extends
        ParameterControlTypeAdapter<T, U> {

    public static final DateTypeAdapter<Date, Date.Builder> INSTANCE =
            new DateTypeAdapter<Date, Date.Builder>() {

                @Override
                protected Date.Builder createBuilderInstance() {
                    return new Date.Builder();
                }

                @Override
                protected Date createInstance(Date.Builder builder) {
                    return builder.create();
                }
            };
    private static final String KEY_MIN = "min";
    private static final String KEY_MAX = "max";

    @Override
    protected void deserialize(JsonObject from, U builder, JsonDeserializationContext
            context) {
        builder.setMin(JsonUtils.getDateTime(from, KEY_MIN, getFormatter()));
        builder.setMax(JsonUtils.getDateTime(from, KEY_MAX, getFormatter()));
        super.deserialize(from, builder, context);
    }

    @Override
    protected void serialize(T from, JsonObject to, JsonSerializationContext context) {
        to.addProperty(KEY_MIN, from.min.toString(getFormatter()));
        to.addProperty(KEY_MAX, from.max.toString(getFormatter()));
        super.serialize(from, to, context);
    }

    protected DateTimeFormatter getFormatter() {
        return Date.FORMATTER;
    }
}
