package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.methods.JsonUtils;
import com.yandex.money.api.model.showcase.components.uicontrol.Number;


/**
 * Base type adapter for subclasses of {@link Number} component.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
abstract class BaseNumberTypeAdapter<T extends Number, U extends Number.Builder>
        extends ParameterControlTypeAdapter<T, U> {

    private static final String MEMBER_MAX = "max";
    private static final String MEMBER_MIN = "min";
    private static final String MEMBER_STEP = "step";

    @Override
    protected void deserialize(JsonObject src, U builder,
                               JsonDeserializationContext context) {
        builder.setMax(JsonUtils.getBigDecimal(src, MEMBER_MAX));
        builder.setMin(JsonUtils.getBigDecimal(src, MEMBER_MIN));
        builder.setStep(JsonUtils.getBigDecimal(src, MEMBER_STEP));
        super.deserialize(src, builder, context);
    }

    @Override
    protected void serialize(T src, JsonObject to, JsonSerializationContext context) {
        to.addProperty(MEMBER_MAX, src.max);
        to.addProperty(MEMBER_MIN, src.min);
        to.addProperty(MEMBER_STEP, src.step);
        super.serialize(src, to, context);
    }
}
