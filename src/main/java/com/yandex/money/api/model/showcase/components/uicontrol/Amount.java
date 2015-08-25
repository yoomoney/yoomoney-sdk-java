package com.yandex.money.api.model.showcase.components.uicontrol;

import com.yandex.money.api.model.showcase.Fee;
import com.yandex.money.api.utils.Currency;
import com.yandex.money.api.utils.ToStringBuilder;

import java.math.BigDecimal;

/**
 * Cost of transaction.
 *
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public final class Amount extends Number {

    /**
     * Currency. Default is {@link Currency#RUB}.
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Amount amount = (Amount) o;

        return currency == amount.currency && fee.equals(amount.fee);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + currency.hashCode();
        result = 31 * result + fee.hashCode();
        return result;
    }

    @Override
    protected ToStringBuilder getToStringBuilder() {
        return new ToStringBuilder("Amount")
                .append("currency", currency.toString())
                .append("fee", fee.toString())
                .append(super.getToStringBuilder());
    }

    /**
     * {@link Amount} builder.
     */
    public static final class Builder extends Number.Builder {

        private static final BigDecimal PENNY = new BigDecimal("0.01");

        private Currency currency = Currency.RUB;
        private Fee fee = Fee.NO_FEE;

        public Builder() {
            super(PENNY, null, PENNY);
        }

        @Override
        public Amount create() {
            setType(Type.AMOUNT);
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
}