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

package com.yandex.money.api.typeadapters;

import com.google.gson.*;
import com.yandex.money.api.model.Card;
import com.yandex.money.api.model.ExternalCard;

import java.lang.reflect.Type;

import static com.yandex.money.api.methods.JsonUtils.getMandatoryString;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public final class ExternalCardTypeAdapter extends BaseTypeAdapter<ExternalCard> {

    private static final ExternalCardTypeAdapter INSTANCE = new ExternalCardTypeAdapter();

    private static final String MEMBER_FUNDING_SOURCE_TYPE = "type";
    private static final String MEMBER_MONEY_SOURCE_TOKEN = "money_source_token";
    private static final String MEMBER_TYPE = "payment_card_type";

    private ExternalCardTypeAdapter() {
    }

    /**
     * @return instance of this class
     */
    public static ExternalCardTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    public ExternalCard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject object = json.getAsJsonObject();
        ExternalCard.Builder builder = new ExternalCard.Builder();
        CardTypeAdapter.Delegate.deserialize(object, builder);
        builder.setFundingSourceType(getMandatoryString(object, MEMBER_FUNDING_SOURCE_TYPE))
                .setMoneySourceToken(getMandatoryString(object, MEMBER_MONEY_SOURCE_TOKEN))
                .setType(Card.Type.parse(getMandatoryString(object, MEMBER_TYPE)));
        return builder.create();
    }

    @Override
    public JsonElement serialize(ExternalCard src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        CardTypeAdapter.Delegate.serialize(object, src);
        object.addProperty(MEMBER_FUNDING_SOURCE_TYPE, src.fundingSourceType);
        object.addProperty(MEMBER_MONEY_SOURCE_TOKEN, src.moneySourceToken);
        object.addProperty(MEMBER_TYPE, src.type.name);
        return object;
    }

    @Override
    protected Class<ExternalCard> getType() {
        return ExternalCard.class;
    }
}
