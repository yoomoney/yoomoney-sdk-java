package com.yandex.money.api.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

/**
 * Fees info.
 */
public class Fees {

    /**
     * Yandex.Money fee. (optional)
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("service")
    public final BigDecimal service;

    /**
     * Сounterparty fee. (optional)
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("counterparty")
    public final BigDecimal counterparty;

    /**
     * Constructor.
     *
     * @param service Yandex.Money fee
     * @param counterparty counterparty fee
     */
    public Fees(BigDecimal service, BigDecimal counterparty) {
        this.service = service;
        this.counterparty = counterparty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Fees fees = (Fees) o;

        //noinspection SimplifiableIfStatement
        if (service != null ? !service.equals(fees.service) : fees.service != null) return false;
        return counterparty != null ? counterparty.equals(fees.counterparty) : fees.counterparty == null;
    }

    @Override
    public int hashCode() {
        int result = service != null ? service.hashCode() : 0;
        result = 31 * result + (counterparty != null ? counterparty.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Fees{" +
                "service=" + service +
                ", counterparty=" + counterparty +
                '}';
    }
}
