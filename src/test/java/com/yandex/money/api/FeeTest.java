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

package com.yandex.money.api;

import com.yandex.money.api.exceptions.IllegalAmountException;
import com.yandex.money.api.model.showcase.AmountType;
import com.yandex.money.api.model.showcase.DefaultFee;
import com.yandex.money.api.model.showcase.Fee;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;

public final class FeeTest {

    @Test
    public void testNegativeNetAmount() {
        final Fee fee = new DefaultFee(Fee.Type.STD, new BigDecimal("0.03"), new BigDecimal("15.00"), BigDecimal.ZERO,
                null, AmountType.AMOUNT);
        // 15.01 is minimum amount
        try {
            Assert.assertEquals(fee.netAmount(new BigDecimal("15.00")), new BigDecimal("0.01"));
            Assert.assertTrue(false, "Test failed.");
        } catch (IllegalAmountException e) {
            Assert.assertEquals(e.minimumAmount, new BigDecimal("15.01"));
        }
        try {
            Assert.assertEquals(fee.netAmount(new BigDecimal("0.02")), new BigDecimal("0.01"));
            Assert.assertTrue(false, "Test failed.");
        } catch (IllegalAmountException e) {
            Assert.assertEquals(e.minimumAmount, new BigDecimal("15.01"));
        }
    }

    @Test
    public void testMinimalNetAmount() throws IllegalAmountException {
        final Fee fee = new DefaultFee(Fee.Type.STD, new BigDecimal("0.03"), new BigDecimal("15.00"), BigDecimal.ZERO,
                null, AmountType.AMOUNT);
        Assert.assertEquals(fee.amount(new BigDecimal("0.05")), new BigDecimal("15.05"));
        Assert.assertEquals(fee.netAmount(new BigDecimal("15.46")), new BigDecimal("0.45"));
        Assert.assertEquals(fee.netAmount(new BigDecimal("15.01")), new BigDecimal("0.01"));
    }

    @Test
    public void testNoCommission() throws IllegalAmountException {
        final BigDecimal amount = new BigDecimal("0.099");
        final Fee fee = new DefaultFee(Fee.Type.STD, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, null,
                AmountType.AMOUNT);
        Assert.assertEquals(amount, fee.amount(amount));
        Assert.assertEquals(amount, fee.netAmount(amount));
    }

    @Test
    public void testAPercent() throws IllegalAmountException {
        final Fee fee = new DefaultFee(Fee.Type.STD, new BigDecimal("0.005"), BigDecimal.ZERO, BigDecimal.ZERO, null,
                AmountType.AMOUNT);
        test("100.50", "100.00", fee);
        test("0.02", "0.01", fee);
    }

    @Test
    public void testBFixedCommission() throws IllegalAmountException {
        test("115.00", "100.00", new DefaultFee(Fee.Type.STD, BigDecimal.ZERO, new BigDecimal("15.00"),
                BigDecimal.ZERO, null, AmountType.AMOUNT));
        test("15.01", "0.01", new DefaultFee(Fee.Type.STD, BigDecimal.ZERO, new BigDecimal("15.00"), BigDecimal.ZERO,
                null, AmountType.AMOUNT));
        test("1.01", "1.00", new DefaultFee(Fee.Type.STD, BigDecimal.ZERO, new BigDecimal("0.001"), BigDecimal.ZERO,
                null, AmountType.AMOUNT));
    }

    @Test
    public void testCMinCommission() throws IllegalAmountException {
        test("115.00", "100.00", new DefaultFee(Fee.Type.STD, BigDecimal.ZERO, BigDecimal.ZERO,
                new BigDecimal("15.00"), null, AmountType.AMOUNT));
        test("15.01", "0.01", new DefaultFee(Fee.Type.STD, BigDecimal.ZERO, BigDecimal.ZERO, new BigDecimal("15.00"),
                null, AmountType.AMOUNT));
        test("1.01", "1.00", new DefaultFee(Fee.Type.STD, BigDecimal.ZERO, BigDecimal.ZERO, new BigDecimal("0.001"),
                null, AmountType.AMOUNT));
        test("100.60", "100.00", new DefaultFee(Fee.Type.STD, new BigDecimal("0.005"), BigDecimal.ZERO,
                new BigDecimal("0.60"), null, AmountType.AMOUNT));
    }

    @Test
    public void testDMaxCommission() throws IllegalAmountException {
        test("100.04", "100.00", new DefaultFee(Fee.Type.STD, new BigDecimal("0.005"), BigDecimal.ZERO,
                BigDecimal.ZERO, new BigDecimal("0.04"), AmountType.AMOUNT));
        test("100.01", "100.00", new DefaultFee(Fee.Type.STD, new BigDecimal("0.005"), BigDecimal.ZERO,
                BigDecimal.ZERO, new BigDecimal("0.004"), AmountType.AMOUNT));
    }

    @Test
    public void testRounding() throws IllegalAmountException {
        // провоцируем периодические дроби
        test("3.44", "3.33", new DefaultFee(Fee.Type.STD, new BigDecimal("0.0333"), BigDecimal.ZERO, BigDecimal.ZERO,
                null, AmountType.AMOUNT));
        test("5.30", "5.00", new DefaultFee(Fee.Type.STD, new BigDecimal("0.06"), BigDecimal.ZERO, BigDecimal.ZERO,
                null, AmountType.AMOUNT));
    }

    /**
     * Тест по контольным значениям ТК 01-24549 из testlink
     */
    @Test
    public void testP2p() throws IllegalAmountException {
        final Fee fee = new DefaultFee(Fee.Type.STD, new BigDecimal("0.005"), BigDecimal.ZERO, BigDecimal.ZERO, null,
                AmountType.AMOUNT);
        test("3.02", "3.00", fee);
        test("1005.00", "1000.00", fee);
        test("0.02", "0.01", fee);
        test("1010.03", "1005.00", fee);
        test("1004.99", "999.99", fee);
        test("1010.01", "1004.99", fee);
        test("1000.00", "995.02", fee);
        test("1010.06", "1005.03", fee);
        test("999.98", "995.00", fee);
        Assert.assertEquals(fee.netAmount(new BigDecimal("3.01")), new BigDecimal("3.00"));
        Assert.assertEquals(fee.netAmount(new BigDecimal("1005.03")), new BigDecimal("1000.03"));
        Assert.assertEquals(fee.amount(new BigDecimal("1005.00")), new BigDecimal("1010.03"));
        Assert.assertEquals(fee.amount(new BigDecimal("995.00")), new BigDecimal("999.98"));
        Assert.assertEquals(fee.netAmount(new BigDecimal("999.97")), new BigDecimal("995.00"));
    }

    private void test(String amount, String netAmount, Fee fee) throws IllegalAmountException {
        BigDecimal bAmount = new BigDecimal(amount);
        BigDecimal bNetAmount = new BigDecimal(netAmount);
        Assert.assertEquals(fee.amount(bNetAmount), bAmount);
        Assert.assertEquals(fee.netAmount(bAmount), bNetAmount);
    }
}
