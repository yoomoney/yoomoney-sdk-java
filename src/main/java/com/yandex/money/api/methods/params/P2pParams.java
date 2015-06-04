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

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Convenience class for P2P payment parameters.
 *
 * @author Dmitriy Melnikov (dvmelnikov@yamoney.ru)
 */
public final class P2pParams extends BaseParams {

    public static final String PATTERN_ID = "p2p";

    private P2pParams(Map<String, String> paymentParams) {
        super(PATTERN_ID, paymentParams);
    }

    /**
     * @author Anton Ermak (ermak@yamoney.ru)
     */
    public static class Builder {

        private final String to;
        private final Boolean isAmountDue;

        private BigDecimal amount;
        private String comment;
        private Boolean codepro;
        private Integer expirePeriod;
        private String label;
        private String message;

        public static Builder createWithAmount(String to, BigDecimal amount) {
            return new Builder(to, amount, false);
        }

        public static Builder createWithAmountDue(String to, BigDecimal amountDue) {
            return new Builder(to, amountDue, true);
        }

        private Builder(String to, BigDecimal amount, boolean isAmountDue) {
            this.to = to;
            this.amount = amount;
            this.isAmountDue = isAmountDue;
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
         * @param codepro {@code true} if payment should be protected whith a code
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

        public P2pParams build() {
            return new P2pParams(makeParams());
        }

        private Map<String, String> makeParams() {
            HashMap<String, String> params = new HashMap<>();
            params.put("pattern_id", "p2p");
            params.put("to", to);
            params.put(isAmountDue ? "amount_due" : "amount", amount.toPlainString());
            params.put("comment", comment);
            params.put("message", message);
            params.put("label", label);
            params.put("codepro", codepro.toString());
            params.put("expire_period", expirePeriod.toString());
            return params;
        }
    }
}
