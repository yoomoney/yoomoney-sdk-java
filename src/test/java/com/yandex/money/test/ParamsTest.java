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

import com.yandex.money.api.methods.params.P2pTransferParams;
import com.yandex.money.api.methods.params.PhoneParams;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Anton Ermak (ermak@yamoney.ru).
 */
public final class ParamsTest {

    private static final String accountNumber = "4100414141414";
    private static final BigDecimal amount = new BigDecimal("10.01");

    /**
     * Tests that {@code amount} or {@code amount_due} is required.
     */
    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testP2pTransferParamsBuilderException() {
        new P2pTransferParams.Builder(accountNumber).create();
    }

    /**
     * Tests that {@code amount} and {@code to} is required for p2p payment.
     */
    @Test
    public void testP2pTransferParamsBuilderCreated() {
        int requiredParamsSize = 2;
        P2pTransferParams params = createP2pBuilderRequired().create();
        Assert.assertEquals(params.paymentParams.size(), requiredParamsSize);
    }

    /**
     * Tests that key-value naming of p2p params matches documentation.
     */
    @Test
    public void testP2pTransferParamsComplete() throws Exception {
        String comment = "Some comment";
        Integer expirePeriod = 5;
        String label = "some label";
        String message = "message";

        Map<String, String> params = createP2pBuilderRequired()
                .setCodepro(true)
                .setComment(comment)
                .setExpirePeriod(expirePeriod)
                .setLabel(label)
                .setMessage(message)
                .create()
                .paymentParams;

        HashMap<String, String> expectedParams = new HashMap<>();
        expectedParams.put("to", accountNumber);
        expectedParams.put("label", label);
        expectedParams.put("amount", amount.toPlainString());
        expectedParams.put("codepro", "true");
        expectedParams.put("message", message);
        expectedParams.put("comment", comment);
        expectedParams.put("expire_period", expirePeriod.toString());

        Assert.assertEquals(params, expectedParams);
    }

    /**
     * Tests that key-value naming of phone topup params matches documentation.
     */
    @Test
    public void testShowParamsComplete() throws Exception {
        String phoneNumber = "79111111111";
        PhoneParams phoneParams = PhoneParams.newInstance(phoneNumber, amount);

        HashMap<String, String> expectedParams = new HashMap<>();
        expectedParams.put("phone-number", phoneNumber);
        expectedParams.put("amount", amount.toPlainString());
        Assert.assertEquals(phoneParams.paymentParams, expectedParams);
    }

    private static P2pTransferParams.Builder createP2pBuilderRequired() {
        return new P2pTransferParams.Builder(accountNumber).setAmount(amount);
    }
}
