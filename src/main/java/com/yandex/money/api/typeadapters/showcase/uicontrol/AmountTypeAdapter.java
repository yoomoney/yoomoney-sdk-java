package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.model.showcase.Fee;
import com.yandex.money.api.model.showcase.components.uicontrol.Amount;
import com.yandex.money.api.utils.Currency;

/**
 * Type adapter for {@link Amount} component.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class AmountTypeAdapter extends NumberTypeAdapter<Amount, Amount.Builder> {

    private static final String KEY_CURRENCY = "currency";
    private static final String KEY_FEE = "fee";

    @Override
    protected void deserialize(JsonObject src, Amount.Builder builder,
                               JsonDeserializationContext context) {

        builder.setCurrency(Currency.parseAlphaCode(src.get("currency").getAsString()));
        builder.setFee((Fee) context.deserialize(src.get("fee"), Fee.class));

        super.deserialize(src, builder, context);
    }

    @Override
    protected void serialize(Amount src, JsonObject to, JsonSerializationContext context) {
        to.addProperty(KEY_CURRENCY, src.currency.alphaCode);
        to.add(KEY_FEE, context.serialize(src.fee, Fee.class));
        super.serialize(src, to, context);
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
