package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.methods.JsonUtils;
import com.yandex.money.api.model.showcase.components.uicontrol.Date;

import org.joda.time.format.DateTimeFormatter;

/**
 * Base type adapter for subclasses of {@link Date} component.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
abstract class BaseDateTypeAdapter<T extends Date, U extends Date.Builder>
        extends ParameterControlTypeAdapter<T, U> {

    private static final String MEMBER_MIN = "min";
    private static final String MEMBER_MAX = "max";

    @Override
    protected final void deserialize(JsonObject src, U builder, JsonDeserializationContext
            context) {
        builder.setMin(JsonUtils.getDateTime(src, MEMBER_MIN, getFormatter()));
        builder.setMax(JsonUtils.getDateTime(src, MEMBER_MAX, getFormatter()));
        super.deserialize(src, builder, context);
    }

    @Override
    protected final void serialize(T src, JsonObject to, JsonSerializationContext context) {
        if(src.min != null) {
            to.addProperty(MEMBER_MIN, src.min.toString(getFormatter()));

        }
        if(src.max != null) {
            to.addProperty(MEMBER_MAX, src.max.toString(getFormatter()));
        }
        super.serialize(src, to, context);
    }

    protected DateTimeFormatter getFormatter() {
        return Date.FORMATTER;
    }
}

