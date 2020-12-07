/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 NBCO YooMoney LLC
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

package com.yoo.money.api.model;

import com.google.gson.annotations.SerializedName;
import com.yoo.money.api.time.YearMonth;

import static com.yoo.money.api.util.Common.checkNotEmpty;
import static com.yoo.money.api.util.Common.checkNotNull;

/**
 * Represents basic bank card data.
 */
public class Card implements BankCardInfo, MoneySource {

    @SerializedName("id")
    public final String id;

    /**
     * panned fragment of card's number
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("pan_fragment")
    public final String panFragment;

    /**
     * type of a card (e.g. VISA, MasterCard, AmericanExpress, etc.)
     */
    @SerializedName("type")
    public final CardBrand type;

    @SuppressWarnings("WeakerAccess")
    protected Card(Builder builder) {
        id = checkNotEmpty(builder.id, "id");
        panFragment = checkNotEmpty(builder.panFragment, "panFragment");
        type = checkNotNull(builder.type, "type");
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getCardholderName() {
        return null;
    }

    @Override
    public String getCardNumber() {
        return panFragment;
    }

    @Override
    public CardBrand getCardBrand() {
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
        //noinspection SimplifiableIfStatement
        if (panFragment != null ? !panFragment.equals(card.panFragment) : card.panFragment != null) return false;
        return type == card.type;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + (panFragment != null ? panFragment.hashCode() : 0);
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id='" + id + '\'' +
                ", panFragment='" + panFragment + '\'' +
                ", type=" + type +
                '}';
    }

    public static class Builder {

        String id;
        String panFragment;
        CardBrand type = CardBrand.UNKNOWN;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setPanFragment(String panFragment) {
            this.panFragment = panFragment;
            return this;
        }

        public Builder setType(CardBrand type) {
            this.type = type;
            return this;
        }

        public Card create() {
            return new Card(this);
        }
    }
}
