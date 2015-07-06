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

package com.yandex.money.test;

import com.yandex.money.api.methods.AccountInfo;
import com.yandex.money.api.model.AccountStatus;
import com.yandex.money.api.model.AccountType;
import com.yandex.money.api.model.Avatar;
import com.yandex.money.api.model.BalanceDetails;
import com.yandex.money.api.model.Card;
import com.yandex.money.api.model.Error;
import com.yandex.money.api.typeadapters.AccountInfoTypeAdapter;
import com.yandex.money.api.typeadapters.AvatarTypeAdapter;
import com.yandex.money.api.typeadapters.BalanceDetailsTypeAdapter;
import com.yandex.money.api.typeadapters.CardTypeAdapter;
import com.yandex.money.api.typeadapters.ErrorTypeAdapter;
import com.yandex.money.api.typeadapters.TypeAdapter;
import com.yandex.money.api.utils.Currency;

import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class ModelTests {

    @Test
    public void testAccountInfo() {
        performTest(createAccountInfo(), AccountInfoTypeAdapter.getInstance());
    }

    @Test
    public void testAvatar() {
        performTest(createAvatar(), AvatarTypeAdapter.getInstance());
    }

    @Test
    public void testBalanceDetails() {
        performTest(createBalanceDetails(), BalanceDetailsTypeAdapter.getInstance());
    }

    @Test
    public void testCard() {
        performTest(createCard(), CardTypeAdapter.getInstance());
    }

    @Test
    public void testError() {
        performTest(Error.TECHNICAL_ERROR, ErrorTypeAdapter.getInstance());
    }

    private static <T> void performTest(T value, TypeAdapter<T> adapter) {
        Assert.assertEquals(adapter.fromJson(adapter.toJsonTree(value)), value);
    }

    private static AccountInfo createAccountInfo() {
        return new AccountInfo("account", BigDecimal.TEN, Currency.RUB,
                AccountStatus.IDENTIFIED, AccountType.PERSONAL, null, createBalanceDetails(),
                Arrays.asList(createCard(), createCard()), Arrays.asList("service1", "service2"));
    }

    private static Avatar createAvatar() {
        return new Avatar("some url", DateTime.now());
    }

    private static BalanceDetails createBalanceDetails() {
        return new BalanceDetails(BigDecimal.ONE, BigDecimal.TEN, null, null,
                null, null);
    }

    private static Card createCard() {
        return new Card("id", "panFragment", Card.Type.MASTER_CARD);
    }
}
