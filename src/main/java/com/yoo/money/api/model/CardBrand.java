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
import com.yoo.money.api.util.Enums;

/**
 * Represents a card brand like VISA or MasterCard.
 */
public enum CardBrand implements Enums.WithCode<CardBrand> {

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
    @SerializedName("Maestro")
    MAESTRO("Maestro", "", 0),
    @SerializedName("Unknown")
    UNKNOWN("Unknown", "CSC", 4);

    /**
     * Name of payment system.
     */
    public final String name;
    /**
     * Card Security Code abbreviation
     */
    public final String cscAbbr;
    /**
     * Card Security Code length
     */
    public final int cscLength;

    CardBrand(String name, String cscAbbr, int cscLength) {
        this.name = name;
        this.cscAbbr = cscAbbr;
        this.cscLength = cscLength;
    }

    @Override
    public String getCode() {
        return name;
    }

    @Override
    public CardBrand[] getValues() {
        return values();
    }
}
