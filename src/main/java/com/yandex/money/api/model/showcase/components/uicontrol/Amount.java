package com.yandex.money.api.model.showcase.components.uicontrol;

import com.yandex.money.api.model.showcase.Fee;
import com.yandex.money.api.utils.Currency;

import java.math.BigDecimal;

/**
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public final class Amount extends Number {

    public final Currency currency;
    public final Fee fee;

    private Amount(Builder builder) {
        super(builder);
        currency = builder.currency;
        fee = builder.fee;
    }

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

    public enum AmountType {
        AMOUNT("amount"),
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