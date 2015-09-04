package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.model.showcase.Fee;
import com.yandex.money.api.model.showcase.components.uicontrol.Amount;
import com.yandex.money.api.utils.Currency;

import java.lang.reflect.Type;

/**
 * Type adapter for {@link Amount} component.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class AmountTypeAdapter extends NumberTypeAdapter<Amount, Amount.Builder> {

    public static final AmountTypeAdapter INSTANCE = new AmountTypeAdapter();

    private static final String MEMBER_CURRENCY = "currency";
    private static final String MEMBER_FEE = "fee";

    private AmountTypeAdapter() {
    }

    @Override
    protected void deserialize(JsonObject src, Amount.Builder builder,
                               JsonDeserializationContext context) {

        builder.setCurrency(Currency.parseAlphaCode(src.get(MEMBER_CURRENCY).getAsString()));
        builder.setFee((Fee) context.deserialize(src.get(MEMBER_FEE), Fee.class));

        super.deserialize(src, builder, context);
    }

    @Override
    protected void serialize(Amount src, JsonObject to, JsonSerializationContext context) {
        to.addProperty(MEMBER_CURRENCY, src.currency.alphaCode);
        to.add(MEMBER_FEE, context.serialize(src.fee, Fee.class));
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

    @Override
    protected Type getType() {
        return Amount.class;
    }
}
