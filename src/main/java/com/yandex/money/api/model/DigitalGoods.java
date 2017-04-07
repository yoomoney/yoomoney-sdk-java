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

package com.yandex.money.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

import static com.yandex.money.api.util.Common.checkNotNull;

/**
 * Digital Goods that can be obtained after payment if available.
 *
 * @see com.yandex.money.api.model.Good
 */
public class DigitalGoods {

    /**
     * not null list of articles
     */
    @SerializedName("article")
    public final List<Good> article;

    /**
     * not null list of bonuses
     */
    @SerializedName("bonus")
    public final List<Good> bonus;

    /**
     * Constructor.
     *
     * @param article main articles
     * @param bonus bonuses
     */
    public DigitalGoods(List<Good> article, List<Good> bonus) {
        this.article = Collections.unmodifiableList(checkNotNull(article, "article"));
        this.bonus = bonus != null ? Collections.unmodifiableList(bonus) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DigitalGoods goods = (DigitalGoods) o;

        if (!article.equals(goods.article)) return false;
        return bonus != null ? bonus.equals(goods.bonus) : goods.bonus == null;
    }

    @Override
    public int hashCode() {
        int result = article.hashCode();
        result = 31 * result + (bonus != null ? bonus.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DigitalGoods{" +
                "article=" + article +
                ", bonus=" + bonus +
                '}';
    }
}
