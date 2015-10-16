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

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.model.Card;

import java.lang.reflect.Type;

import static com.yandex.money.api.typeadapters.JsonUtils.getString;
import static com.yandex.money.api.utils.Common.checkNotNull;

/**
 * Type adapter for {@link Card}.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public final class CardTypeAdapter extends BaseTypeAdapter<Card> {

    private static final CardTypeAdapter INSTANCE = new CardTypeAdapter();

    private CardTypeAdapter() {
    }

    /**
     * @return instance of this class
     */
    public static CardTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    public Card deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject object = json.getAsJsonObject();
        Card.Builder builder = new Card.Builder();
        Delegate.deserialize(object, builder);
        return builder.create();
    }

    @Override
    public JsonElement serialize(Card src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        Delegate.serialize(object, src);
        return object;
    }

    @Override
    protected Class<Card> getType() {
        return Card.class;
    }

    static final class Delegate {

        private static final String MEMBER_PAN_FRAGMENT = "pan_fragment";
        private static final String MEMBER_TYPE = "type";

        private Delegate() {
        }

        static <T extends Card.Builder> void deserialize(JsonObject object, T builder) {
            checkNotNull(object, "object");
            checkNotNull(builder, "builder");
            builder.setPanFragment(getString(object, MEMBER_PAN_FRAGMENT))
                    .setType(Card.Type.parse(getString(object, MEMBER_TYPE)));
            MoneySourceTypeAdapter.Delegate.deserialize(object, builder);
        }

        static <T extends Card> void serialize(JsonObject object, T value) {
            checkNotNull(object, "object");
            checkNotNull(value, "value");
            object.addProperty(MEMBER_PAN_FRAGMENT, value.panFragment);
            object.addProperty(MEMBER_TYPE, value.type.name);
            MoneySourceTypeAdapter.Delegate.serialize(object, value);
        }
    }
}
