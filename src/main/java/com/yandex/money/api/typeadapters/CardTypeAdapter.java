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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.methods.JsonUtils;
import com.yandex.money.api.model.Card;

import java.lang.reflect.Type;

/**
 * Type adapter for {@link Card}.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public final class CardTypeAdapter implements TypeAdapter<Card> {

    private static final CardTypeAdapter INSTANCE = new CardTypeAdapter();

    private static final String MEMBER_ID = "id";
    private static final String MEMBER_PAN_FRAGMENT = "pan_fragment";
    private static final String MEMBER_TYPE = "type";

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Card.class, INSTANCE)
            .create();

    private CardTypeAdapter() {
    }

    /**
     * @return instance of this class
     */
    public static CardTypeAdapter getInstance() {
        return INSTANCE;
    }

    /**
     * Creates {@link Card} from json.
     *
     * @param json json string
     * @return card instance
     */
    public static Card fromJson(String json) {
        return GSON.fromJson(json, Card.class);
    }

    /**
     * Creates {@link Card} from json.
     *
     * @param element json element
     * @return card instance
     */
    public static Card fromJson(JsonElement element) {
        return GSON.fromJson(element, Card.class);
    }

    /**
     * Serializes {@link Card} to json string.
     *
     * @param card card
     * @return json string
     */
    public static String toJson(Card card) {
        return GSON.toJson(card);
    }

    /**
     * Serializes {@link Card} to json tree.
     *
     * @param card card
     * @return json element
     */
    public static JsonElement toJsonTree(Card card) {
        return GSON.toJsonTree(card);
    }

    @Override
    public Card deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        return new Card(JsonUtils.getString(object, MEMBER_ID),
                JsonUtils.getString(object, MEMBER_PAN_FRAGMENT),
                Card.Type.parse(JsonUtils.getString(object, MEMBER_TYPE)));
    }

    @Override
    public JsonElement serialize(Card src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty(MEMBER_ID, src.id);
        object.addProperty(MEMBER_PAN_FRAGMENT, src.panFragment);
        object.addProperty(MEMBER_TYPE, src.type.name);
        return object;
    }
}
