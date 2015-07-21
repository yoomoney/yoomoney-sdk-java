package com.yandex.money.api.model.showcase.components.uicontrol;

import java.math.BigDecimal;

/**
 * Numeric control.
 *
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public class Number extends ParameterControl {

    /**
     * Minimum acceptable number.
     */
    public final BigDecimal min;

    /**
     * Maximum acceptable number.
     */
    public final BigDecimal max;

    /**
     * Step scale factor. The default is {@link BigDecimal#ONE}.
     */
    public final BigDecimal step;

    protected Number(Builder builder) {
        super(builder);
        if (builder.min != null && builder.max != null && builder.min.compareTo(builder.max) > 0) {
            throw new IllegalArgumentException("min > max");
        }
        min = builder.min;
        max = builder.max;
        step = builder.step;
    }

    @Override
    public boolean isValid(String value) {
        return super.isValid(value) && isValidInner(value);
    }

    private boolean isValidInner(String value) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        try {
            BigDecimal v = new BigDecimal(value);
            BigDecimal quotient = v.divideToIntegralValue(step);
            return (min == null || v.compareTo(min) >= 0) &&
                    (max == null || v.compareTo(max) <= 0) &&
                    v.compareTo(quotient.multiply(step)) == 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * {@link Number} builder.
     */
    public static class Builder extends ParameterControl.Builder {

        protected BigDecimal min;
        protected BigDecimal max;
        protected BigDecimal step = BigDecimal.ONE;

        public Builder() {
            super();
        }

        protected Builder(BigDecimal min, BigDecimal max, BigDecimal step) {
            super();
            this.min = min;
            this.max = max;
            this.step = step;
        }

        @Override
        public Number create() {
            return new Number(this);
        }

        public Builder setMin(BigDecimal min) {
            this.min = min;
            return this;
        }

        public Builder setMax(BigDecimal max) {
            this.max = max;
            return this;
        }

        public final Builder setStep(BigDecimal step) {
            if (step != null) {
                this.step = step;
            }
            return this;
        }
    }
}
