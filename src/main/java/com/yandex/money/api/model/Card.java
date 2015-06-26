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

import com.google.gson.JsonElement;
import com.yandex.money.api.typeadapters.CardTypeAdapter;

import java.util.Objects;

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
     * @deprecated use {@link CardTypeAdapter#fromJson(JsonElement)} instead
     */
    @Deprecated
    public static Card createFromJson(JsonElement element) {
        return CardTypeAdapter.getInstance().fromJson(element);
    }

    /**
     * Creates {@link com.yandex.money.api.model.Card} from JSON.
     * @deprecated use {@link CardTypeAdapter#fromJson(String)} instead
     */
    @Deprecated
    public static Card createFromJson(String json) {
        return CardTypeAdapter.getInstance().fromJson(json);
    }

    @Override
    public String toString() {
        return "Card{" +
                "panFragment='" + panFragment + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof Card) {
            Card card = (Card) obj;
            return id.equals(card.id) && type == card.type &&
                    Objects.equals(panFragment, card.panFragment);
        }
        return false;
    }

    /**
     * Serializes {@link com.yandex.money.api.model.Card} object to JSON text.
     *
     * @return JSON text
     * @deprecated use {@link CardTypeAdapter#toJson(Object)} instead
     */
    @Deprecated
    public String serializeToJson() {
        return CardTypeAdapter.getInstance().toJson(this);
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
}
