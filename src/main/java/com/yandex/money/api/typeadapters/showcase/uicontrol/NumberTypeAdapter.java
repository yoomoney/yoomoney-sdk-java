package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.methods.JsonUtils;
import com.yandex.money.api.model.showcase.components.uicontrol.Number;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
abstract class NumberTypeAdapter<T extends Number, U extends Number.Builder> extends
        ParameterControlTypeAdapter<T, U> {

    public static final NumberTypeAdapter<Number, Number.Builder> INSTANCE =
        new NumberTypeAdapter<Number, Number.Builder>() {

            @Override
            protected Number.Builder createBuilderInstance() {
                                                           return new Number.Builder();
                                                                                                                              }

            @Override
            protected Number createInstance(Number.Builder builder) {
                                                                  return builder.create();
                                                                                                                                        }
        };
    private static final String KEY_MAX = "max";
    private static final String KEY_MIN = "min";
    private static final String KEY_STEP = "step";

    @Override
    protected void deserialize(JsonObject from, U builder,
                               JsonDeserializationContext context) {
        builder.setMax(JsonUtils.getBigDecimal(from, KEY_MAX));
        builder.setMin(JsonUtils.getBigDecimal(from, KEY_MIN));
        builder.setStep(JsonUtils.getBigDecimal(from, KEY_STEP));
        super.deserialize(from, builder, context);
    }

    @Override
    protected void serialize(T from, JsonObject to, JsonSerializationContext context) {
        to.addProperty(KEY_MAX, from.max);
        to.addProperty(KEY_MIN, from.min);
        to.addProperty(KEY_STEP, from.step);
        super.serialize(from, to, context);
    }
}
