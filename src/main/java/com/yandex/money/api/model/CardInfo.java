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

import static com.yandex.money.api.util.Common.checkNotEmpty;
import static com.yandex.money.api.util.Common.checkNotNull;

/**
 * Represents bank card data from account info.
 */
public class CardInfo {
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
    protected CardInfo(Builder builder) {
        panFragment = checkNotEmpty(builder.panFragment, "panFragment");
        type = checkNotNull(builder.type, "type");
    }

    public String getCardNumber() {
        return panFragment;
    }

    public CardBrand getCardBrand() {
        return type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Card card = (Card) o;

        //noinspection SimplifiableIfStatement
        if (panFragment != null ? !panFragment.equals(card.panFragment) : card.panFragment != null) return false;
        return type == card.type;
    }

    @Override
    public int hashCode() {
        int result = panFragment.hashCode();
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CardInfo{" +
                "panFragment='" + panFragment + '\'' +
                ", type=" + type +
                '}';
    }

    public static class Builder {
        String panFragment;
        CardBrand type = CardBrand.UNKNOWN;


        public Builder setPanFragment(String panFragment) {
            this.panFragment = panFragment;
            return this;
        }

        public Builder setType(CardBrand type) {
            this.type = type;
            return this;
        }

        public CardInfo create() {
            return new CardInfo(this);
        }
    }
}
