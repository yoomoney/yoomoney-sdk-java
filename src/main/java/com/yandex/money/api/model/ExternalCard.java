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

package com.yandex.money.api.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.api.methods.JsonUtils;
import com.yandex.money.api.utils.Strings;

/**
 * Represents card that not bound to an account.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class ExternalCard extends Card {

    public final String fundingSourceType;
    public final String moneySourceToken;

    /**
     * Constructor.
     *
     * @param panFragment panned fragment of card's number
     * @param type type of a card
     */
    public ExternalCard(String panFragment, Type type, String fundingSourceType,
                        String moneySourceToken) {

        super(null, panFragment, type);
        if (Strings.isNullOrEmpty(fundingSourceType)) {
            throw new NullPointerException("fundingSourceType is null or empty");
        }
        if (Strings.isNullOrEmpty(moneySourceToken)) {
            throw new NullPointerException("money source token is null or empty");
        }
        this.fundingSourceType = fundingSourceType;
        this.moneySourceToken = moneySourceToken;
    }

    public static ExternalCard createFromJson(JsonElement jsonElement) {
        return buildGson().fromJson(jsonElement, ExternalCard.class);
    }

    @Override
    public String toString() {
        return "ExternalCard{" +
                "fundingSourceType='" + fundingSourceType + '\'' +
                ", moneySourceToken='" + moneySourceToken + '\'' +
                '}';
    }

    private static Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(ExternalCard.class, new Deserializer())
                .create();
    }

    private static final class Deserializer implements JsonDeserializer<ExternalCard> {
        @Override
        public ExternalCard deserialize(JsonElement json, java.lang.reflect.Type typeOfT,
                                        JsonDeserializationContext context)
                throws JsonParseException {

            JsonObject object = json.getAsJsonObject();
            return new ExternalCard(JsonUtils.getMandatoryString(object, "pan_fragment"),
                    Type.parse(JsonUtils.getMandatoryString(object, "payment_card_type")),
                    JsonUtils.getMandatoryString(object, "type"),
                    JsonUtils.getMandatoryString(object, "money_source_token"));
        }
    }
}
