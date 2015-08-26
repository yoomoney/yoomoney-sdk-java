package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.model.showcase.Fee;
import com.yandex.money.api.model.showcase.components.uicontrol.Amount;
import com.yandex.money.api.utils.Currency;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class AmountTypeAdapter extends NumberTypeAdapter<Amount, Amount.Builder> {

    private static final String KEY_CURRENCY = "currency";
    private static final String KEY_FEE = "fee";

    @Override
    protected void deserialize(JsonObject from, Amount.Builder builder,
                               JsonDeserializationContext context) {

        builder.setCurrency(Currency.parseAlphaCode(from.get("currency").getAsString()));
        builder.setFee((Fee) context.deserialize(from.get("fee"), Fee.class));

        super.deserialize(from, builder, context);
    }

    @Override
    protected void serialize(Amount from, JsonObject to, JsonSerializationContext context) {
        to.addProperty(KEY_CURRENCY, from.currency.alphaCode);
        to.add(KEY_FEE, context.serialize(from.fee, Fee.class));
        super.serialize(from, to, context);
    }

    @Override
    protected Amount.Builder createBuilderInstance() {
        return new Amount.Builder();
    }

    @Override
    protected Amount createInstance(Amount.Builder builder) {
        return builder.create();
    }
}
