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

import com.yandex.money.api.time.YearMonth;
import com.yandex.money.api.util.Enums;

import static com.yandex.money.api.util.Common.checkNotNull;

/**
 * Bank card info.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class Card extends MoneySource implements BankCardInfo {

    /**
     * name of cardholder
     */
    public final String cardholderName;

    /**
     * panned fragment of card's number
     */
    public final String panFragment;

    /**
     * type of a card (e.g. VISA, MasterCard, AmericanExpress, etc.)
     */
    public final Type type;

    protected Card(Builder builder) {
        super(builder);
        cardholderName = builder.cardholderName;
        panFragment = builder.panFragment;
        type = checkNotNull(builder.type, "type");
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
    public String toString() {
        return "Card{" +
                "id='" + id + '\'' +
                ", panFragment='" + panFragment + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Card card = (Card) o;

        return !(panFragment != null ? !panFragment.equals(card.panFragment) : card.panFragment != null) &&
                type == card.type;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (panFragment != null ? panFragment.hashCode() : 0);
        result = 31 * result + type.hashCode();
        return result;
    }

    public enum Type implements Enums.WithCode<Type> {

        VISA("VISA", "CVV2", 3),
        MASTER_CARD("MasterCard", "CVC2", 3),
        AMERICAN_EXPRESS("AmericanExpress", "CID", 4), // also cscAbbr = 4DBC
        JCB("JCB", "CAV2", 3),
        MIR("Mir", "CSC", 3),
        UNION_PAY("UnionPay", "CVN2", 3),
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

    public static class Builder extends MoneySource.Builder {

        String cardholderName;
        String panFragment;
        Type type = Type.UNKNOWN;

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

        @Override
        public Card create() {
            return new Card(this);
        }
    }
}
