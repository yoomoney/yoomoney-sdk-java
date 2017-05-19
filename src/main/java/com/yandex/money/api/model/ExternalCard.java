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

import static com.yandex.money.api.util.Common.checkNotEmpty;
import static com.yandex.money.api.util.Common.checkNotNull;

/**
 * Represents card that not bound to an account.
 */
public class ExternalCard implements BankCardInfo, MoneySource {

    @SerializedName("payment_card_type")
    public final CardBrand type;
    @SuppressWarnings("WeakerAccess")
    @SerializedName("pan_fragment")
    public final String panFragment;
    @SuppressWarnings("WeakerAccess")
    @SerializedName("type")
    public final String fundingSourceType;
    @SerializedName("money_source_token")
    public final String moneySourceToken;

    @SuppressWarnings("WeakerAccess")
    protected ExternalCard(Builder builder) {
        type = checkNotNull(builder.type, "type");
        panFragment = checkNotEmpty(builder.panFragment, "panFragment");
        fundingSourceType = checkNotEmpty(builder.fundingSourceType, "fundingSourceType");
        moneySourceToken = checkNotEmpty(builder.moneySourceToken, "moneySourceToken");
    }

    @Override
    public String getId() {
        return moneySourceToken;
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

        ExternalCard that = (ExternalCard) o;

        if (type != that.type) return false;
        if (!panFragment.equals(that.panFragment)) return false;
        //noinspection SimplifiableIfStatement
        if (!fundingSourceType.equals(that.fundingSourceType)) return false;
        return moneySourceToken.equals(that.moneySourceToken);
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + panFragment.hashCode();
        result = 31 * result + fundingSourceType.hashCode();
        result = 31 * result + moneySourceToken.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "ExternalCard{" +
                "type=" + type +
                ", panFragment='" + panFragment + '\'' +
                ", fundingSourceType='" + fundingSourceType + '\'' +
                ", moneySourceToken='" + moneySourceToken + '\'' +
                '}';
    }

    public static class Builder {

        CardBrand type;
        String panFragment;
        String fundingSourceType;
        String moneySourceToken;

        public Builder setType(CardBrand type) {
            this.type = type;
            return this;
        }

        public Builder setPanFragment(String panFragment) {
            this.panFragment = panFragment;
            return this;
        }

        public Builder setFundingSourceType(String fundingSourceType) {
            this.fundingSourceType = fundingSourceType;
            return this;
        }

        public Builder setMoneySourceToken(String moneySourceToken) {
            this.moneySourceToken = moneySourceToken;
            return this;
        }

        public ExternalCard create() {
            return new ExternalCard(this);
        }
    }
}
