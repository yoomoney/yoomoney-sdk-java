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

import com.google.gson.annotations.SerializedName;
import com.yandex.money.api.time.YearMonth;
import com.yandex.money.api.util.Enums;

import static com.yandex.money.api.util.Common.checkNotEmpty;
import static com.yandex.money.api.util.Common.checkNotNull;

/**
 * Bank card info.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class Card implements BankCardInfo {

    @SerializedName("id")
    public final String id;
    /**
     * name of cardholder
     */
    @SerializedName("cardholder_name")
    public final String cardholderName;

    /**
     * panned fragment of card's number
     */
    @SerializedName("pan_fragment")
    public final String panFragment;

    /**
     * type of a card (e.g. VISA, MasterCard, AmericanExpress, etc.)
     */
    @SerializedName("type")
    public final Type type;

    protected Card(Builder builder) {
        id = checkNotEmpty(builder.id, "id");
        cardholderName = builder.cardholderName;
        panFragment = builder.panFragment;
        type = checkNotNull(builder.type, "type");
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getCardholderName() {
        return cardholderName;
    }

    @Override
    public String getCardNumber() {
        return panFragment;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public YearMonth getExpiry() {
        return null;
    }

    @Override
    public boolean isContactless() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        if (!id.equals(card.id)) return false;
        if (cardholderName != null ? !cardholderName.equals(card.cardholderName) : card.cardholderName != null)
            return false;
        if (panFragment != null ? !panFragment.equals(card.panFragment) : card.panFragment != null) return false;
        return type == card.type;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (cardholderName != null ? cardholderName.hashCode() : 0);
        result = 31 * result + (panFragment != null ? panFragment.hashCode() : 0);
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id='" + id + '\'' +
                ", cardholderName='" + cardholderName + '\'' +
                ", panFragment='" + panFragment + '\'' +
                ", type=" + type +
                '}';
    }

    public enum Type implements Enums.WithCode<Type> {

        @SerializedName("VISA")
        VISA("VISA", "CVV2", 3),
        @SerializedName("MasterCard")
        MASTER_CARD("MasterCard", "CVC2", 3),
        @SerializedName("AmericanExpress")
        AMERICAN_EXPRESS("AmericanExpress", "CID", 4), // also cscAbbr = 4DBC
        @SerializedName("JCB")
        JCB("JCB", "CAV2", 3),
        @SerializedName("Mir")
        MIR("Mir", "CSC", 3),
        @SerializedName("UnionPay")
        UNION_PAY("UnionPay", "CVN2", 3),
        @SerializedName("Unknown")
        UNKNOWN("Unknown", "CSC", 4);

        public final String name;
        public final String cscAbbr;
        public final int cscLength;

        Type(String name, String cscAbbr, int cscLength) {
            this.name = name;
            this.cscAbbr = cscAbbr;
            this.cscLength = cscLength;
        }

        @Override
        public String getCode() {
            return name;
        }

        @Override
        public Type[] getValues() {
            return values();
        }

        public static Type parse(String name) {
            return Enums.parseIgnoreCase(UNKNOWN, UNKNOWN, name);
        }
    }

    public static class Builder {

        String id;
        String cardholderName;
        String panFragment;
        Type type = Type.UNKNOWN;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setCardholderName(String cardholderName) {
            this.cardholderName = cardholderName;
            return this;
        }

        public Builder setPanFragment(String panFragment) {
            this.panFragment = panFragment;
            return this;
        }

        public Builder setType(Type type) {
            this.type = type;
            return this;
        }

        public Card create() {
            return new Card(this);
        }
    }
}
