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

package com.yandex.money.api.methods.params;

import com.yandex.money.api.utils.Strings;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Convenience class for P2P payment parameters.
 *
 * @author Dmitriy Melnikov (dvmelnikov@yamoney.ru)
 */
public final class P2pTransferParams extends PaymentParams {

    public static final String PATTERN_ID = "p2p";

    private P2pTransferParams(Map<String, String> paymentParams) {
        super(PATTERN_ID, paymentParams);
    }

    /**
     * @author Slava Yasevich (vyasevich@yamoney.ru)
     */
    public static final class Builder {

        private final String to;

        private BigDecimal amount;
        private BigDecimal amountDue;
        private String comment;
        private Boolean codepro;
        private Integer expirePeriod;
        private String label;
        private String message;

        public Builder(String to) {
            Strings.checkNotNullAndNotEmpty(to, "to");
            this.to = to;
        }

        /**
         * @param amount amount to pay
         */
        public Builder setAmount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        /**
         * @param amountDue amount to receive
         */
        public Builder setAmountDue(BigDecimal amountDue) {
            this.amountDue = amountDue;
            return this;
        }

        /**
         * @param comment payment comment
         */
        public Builder setComment(String comment) {
            this.comment = comment;
            return this;
        }

        /**
         * @param message message to a recipient
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * @param label payment label
         */
        public Builder setLabel(String label) {
            this.label = label;
            return this;
        }

        /**
         * @param codepro {@code true} if payment should be protected with a code
         */
        public Builder setCodepro(Boolean codepro) {
            this.codepro = codepro;
            return this;
        }

        /**
         * @param expirePeriod number of days during which a transfer can be received
         */
        public Builder setExpirePeriod(Integer expirePeriod) {
            this.expirePeriod = expirePeriod;
            return this;
        }

        public P2pTransferParams build() {
            return new P2pTransferParams(makeParams());
        }

        private Map<String, String> makeParams() {
            HashMap<String, String> params = new HashMap<>();

            params.put(Params.TO.paramName, to);
            setAmount(params);

            if (comment != null) {
                params.put(Params.COMMENT.paramName, comment);
            }
            if (message != null) {
                params.put(Params.MESSAGE.paramName, message);
            }
            if (label != null) {
                params.put(Params.LABEL.paramName, label);
            }
            if (codepro != null) {
                params.put(Params.CODEPRO.paramName, codepro.toString());
            }
            if (expirePeriod != null) {
                params.put(Params.EXPIRE.paramName, expirePeriod.toString());
            }
            return params;
        }

        private void setAmount(Map<String, String> params) {
            if (amount == null && amountDue == null) {
                throw new IllegalStateException("One of \"amount\" and \"amountDue\" is mandatory");
            }
            if (amount == null) {
                params.put(Params.AMOUNT_DUE.paramName, amountDue.toPlainString());
            } else {
                params.put(Params.AMOUNT.paramName, amount.toPlainString());
            }
        }

        private enum Params {

            AMOUNT_DUE("amount_due"),
            AMOUNT("amount"),
            CODEPRO("codepro"),
            COMMENT("comment"),
            EXPIRE("expire_period"),
            LABEL("label"),
            MESSAGE("message"),
            TO("to");

            public final String paramName;

            Params(String paramName) {
                this.paramName = paramName;
            }
        }
    }
}
