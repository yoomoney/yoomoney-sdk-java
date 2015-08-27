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
public abstract class DateTypeAdapter<T extends Date, U extends Date.Builder> extends
        ParameterControlTypeAdapter<T, U> {

    /**
     * Type adapter for {@link Date} component.
     */
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
    private static final String MEMBER_MIN = "min";
    private static final String MEMBER_MAX = "max";

    @Override
    protected void deserialize(JsonObject src, U builder, JsonDeserializationContext
            context) {
        builder.setMin(JsonUtils.getDateTime(src, MEMBER_MIN, getFormatter()));
        builder.setMax(JsonUtils.getDateTime(src, MEMBER_MAX, getFormatter()));
        super.deserialize(src, builder, context);
    }

    @Override
    protected void serialize(T src, JsonObject to, JsonSerializationContext context) {
        to.addProperty(MEMBER_MIN, src.min.toString(getFormatter()));
        to.addProperty(MEMBER_MAX, src.max.toString(getFormatter()));
        super.serialize(src, to, context);
    }

    protected DateTimeFormatter getFormatter() {
        return Date.FORMATTER;
    }
}
