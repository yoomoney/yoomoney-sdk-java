package com.yandex.money.api.model.showcase.components.uicontrol;

import com.yandex.money.api.model.showcase.Fee;
import com.yandex.money.api.utils.Currency;

import java.math.BigDecimal;

/**
 * The cost of transaction.
 *
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public final class Amount extends Number {

    /**
     * The currency. Default is {@link Currency#RUB}.
     */
    public final Currency currency;

    /**
     * Fee. Default is {@link Fee#NO_FEE}.
     */
    public final Fee fee;

    private Amount(Builder builder) {
        super(builder);
        currency = builder.currency;
        fee = builder.fee;
    }

    /**
     * {@link Amount} builder.
     */
    public static final class Builder extends Number.Builder {

        private static final BigDecimal PENNY = new BigDecimal("0.01");

        private Currency currency = Currency.RUB;
        private Fee fee;

        public Builder() {
            super(PENNY, null, PENNY);
        }

        @Override
        public Amount create() {
            return new Amount(this);
        }

        @Override
        public Builder setMin(BigDecimal min) {
            if (min != null) {
                super.setMin(min);
            }
            return this;
        }

        public Builder setCurrency(Currency currency) {
            if (currency != null) {
                this.currency = currency;
            }
            return this;
        }

        public Builder setFee(Fee fee) {
            this.fee = fee;
            return this;
        }
    }

    /**
     * The type of input.
     */
    public enum AmountType {
        /**
         * Specifies that sum will be debited from the payee account.
         */
        AMOUNT("amount"),

        /**
         * Specifies that sum sum will be received by a recipient.
         */
        NET_AMOUNT("netAmount");

        public final String code;

        public static AmountType parse(String typeName) {
            for (AmountType type : values()) {
                if (type.code.equalsIgnoreCase(typeName)) {
                    return type;
                }
            }
            return null;
        }

        AmountType(String code) {
            this.code = code;
        }
    }
}