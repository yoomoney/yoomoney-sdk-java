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
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.yandex.money.api.methods.JsonUtils;

/**
 * Bank card info.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class Card extends MoneySource {

    /**
     * panned fragment of card's number
     */
    public final String panFragment;

    /**
     * type of a card (e.g. VISA, MasterCard, AmericanExpress, etc.)
     */
    public final Type type;

    /**
     * Constructor.
     *
     * @param id unique card id
     * @param panFragment panned fragment of card's number
     * @param type type of a card
     */
    public Card(String id, String panFragment, Type type) {
        super(id);
        if (type == null) {
            throw new NullPointerException("type is null");
        }
        this.panFragment = panFragment;
        this.type = type;
    }

    /**
     * Creates {@link com.yandex.money.api.model.Card} from {@link com.google.gson.JsonElement}.
     */
    public static Card createFromJson(JsonElement element) {
        return buildGson().fromJson(element, Card.class);
    }

    /**
     * Creates {@link com.yandex.money.api.model.Card} from JSON.
     */
    public static Card createFromJson(String json) {
        return buildGson().fromJson(json, Card.class);
    }

    @Override
    public String toString() {
        return "Card{" +
                "panFragment='" + panFragment + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    /**
     * Serializes {@link com.yandex.money.api.model.Card} object to JSON text.
     *
     * @return JSON text
     */
    public String serializeToJson() {
        return buildGson().toJson(this);
    }

    private static Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(Card.class, new TypeAdapter())
                .create();
    }

    public enum Type {
        VISA("VISA", "CVV2", 3),
        MASTER_CARD("MasterCard", "CVC2", 3),
        AMERICAN_EXPRESS("AmericanExpress", "CID", 4), // also cscAbbr = 4DBC
        JCB("JCB", "CAV2", 3),
        UNKNOWN("UNKNOWN", "CSC", 4);

        public final String name;
        public final String cscAbbr;
        public final int cscLength;

        Type(String name, String cscAbbr, int cscLength) {
            this.name = name;
            this.cscAbbr = cscAbbr;
            this.cscLength = cscLength;
        }

        public static Type parse(String name) {
            if (name == null) {
                return UNKNOWN;
            }
            for (Type cardType : values()) {
                if (cardType.name.equalsIgnoreCase(name)) {
                    return cardType;
                }
            }
            return UNKNOWN;
        }
    }

    private static final class TypeAdapter
            implements JsonDeserializer<Card>, JsonSerializer<Card> {

        private static final String FIELD_ID = "id";
        private static final String FIELD_PAN_FRAGMENT = "pan_fragment";
        private static final String FIELD_TYPE = "type";

        @Override
        public Card deserialize(JsonElement json, java.lang.reflect.Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {

            JsonObject object = json.getAsJsonObject();
            return new Card(JsonUtils.getString(object, FIELD_ID),
                    JsonUtils.getString(object, FIELD_PAN_FRAGMENT),
                    Type.parse(JsonUtils.getString(object, FIELD_TYPE)));
        }

        @Override
        public JsonElement serialize(Card src, java.lang.reflect.Type typeOfSrc,
                                     JsonSerializationContext context) {

            JsonObject object = new JsonObject();
            object.addProperty(FIELD_ID, src.id);
            object.addProperty(FIELD_PAN_FRAGMENT, src.panFragment);
            object.addProperty(FIELD_TYPE, src.type.name);
            return object;
        }
    }
}
