package com.yandex.money.api.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

import static com.yandex.money.api.util.Common.checkNotNull;

public class SpendingCategory {

    /**
     * Name of the category
     */
    @SerializedName("name")
    public final String name;

    /**
     * Spending sum of this category
     */
    @SerializedName("sum")
    public final BigDecimal sum;

    /**
     * Constructor.
     *
     * @param name name of this category
     * @param sum spending sum of this category
     */
    public SpendingCategory(String name, BigDecimal sum) {
        this.name = checkNotNull(name, "name");
        this.sum = checkNotNull(sum, "sum");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpendingCategory that = (SpendingCategory) o;

        if (!name.equals(that.name)) return false;
        return sum.equals(that.sum);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + sum.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SpendingCategory{" +
                "name='" + name + '\'' +
                ", sum=" + sum +
                '}';
    }
}
