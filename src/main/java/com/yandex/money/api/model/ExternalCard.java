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

import com.yandex.money.api.utils.Strings;

/**
 * Represents card that not bound to an account.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class ExternalCard extends Card {

    public final String fundingSourceType;
    public final String moneySourceToken;

    protected ExternalCard(Builder builder) {
        super(builder);
        if (Strings.isNullOrEmpty(builder.fundingSourceType)) {
            throw new IllegalArgumentException("fundingSourceType is null or empty");
        }
        if (Strings.isNullOrEmpty(builder.moneySourceToken)) {
            throw new IllegalArgumentException("money source token is null or empty");
        }
        fundingSourceType = builder.fundingSourceType;
        moneySourceToken = builder.moneySourceToken;
    }

    @Override
    public String toString() {
        return "ExternalCard{" +
                "id='" + id + '\'' +
                ", panFragment='" + panFragment + '\'' +
                ", type=" + type +
                ", fundingSourceType='" + fundingSourceType + '\'' +
                ", moneySourceToken='" + moneySourceToken + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ExternalCard that = (ExternalCard) o;

        return fundingSourceType.equals(that.fundingSourceType) &&
                moneySourceToken.equals(that.moneySourceToken);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + fundingSourceType.hashCode();
        result = 31 * result + moneySourceToken.hashCode();
        return result;
    }

    public static class Builder extends Card.Builder {

        private String fundingSourceType;
        private String moneySourceToken;

        public Builder setFundingSourceType(String fundingSourceType) {
            this.fundingSourceType = fundingSourceType;
            return this;
        }

        public Builder setMoneySourceToken(String moneySourceToken) {
            this.moneySourceToken = moneySourceToken;
            return this;
        }

        @Override
        public ExternalCard create() {
            return new ExternalCard(this);
        }
    }
}
