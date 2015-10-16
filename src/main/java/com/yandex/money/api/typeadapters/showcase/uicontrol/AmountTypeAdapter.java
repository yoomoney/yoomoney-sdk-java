/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 NBCO Yandex.Money LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.model.showcase.Fee;
import com.yandex.money.api.model.showcase.components.uicontrols.Amount;
import com.yandex.money.api.typeadapters.FeeTypeAdapter;
import com.yandex.money.api.utils.Currency;

/**
 * Type adapter for {@link Amount} component.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class AmountTypeAdapter extends BaseNumberTypeAdapter<Amount, Amount.Builder> {

    private static final AmountTypeAdapter INSTANCE = new AmountTypeAdapter();

    private static final String MEMBER_CURRENCY = "currency";
    private static final String MEMBER_FEE = "fee";

    private AmountTypeAdapter() {
        // register FeeTypeAdapter to GSON
        FeeTypeAdapter.getInstance();
    }

    /**
     * @return instance of this class
     */
    public static AmountTypeAdapter getInstance() {
        return INSTANCE;
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
    protected Class<Amount> getType() {
        return Amount.class;
    }
}
