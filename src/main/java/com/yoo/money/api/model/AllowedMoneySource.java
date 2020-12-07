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
 * Type of money sources allowed to make a payment.
 */
public enum AllowedMoneySource implements Enums.WithCode<AllowedMoneySource> {

    /**
     * Cards that are linked to a wallet.
     */
    @SerializedName("cards")
    CARDS("cards"),

    /**
     * Cash.
     */
    @SerializedName("cash")
    CASH("cash"),

    /**
     * Any bank card, excluding ones that are linked to a wallet.
     */
    @SerializedName("payment-card")
    PAYMENT_CARD("payment-card"),

    /**
     * Wallet.
     */
    @SerializedName("wallet")
    WALLET("wallet");

    public final String code;

    AllowedMoneySource(String code) {
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public AllowedMoneySource[] getValues() {
        return values();
    }
}
