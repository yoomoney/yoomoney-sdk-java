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

package com.yandex.money.test.showcase;

import com.yandex.money.api.model.showcase.components.uicontrols.Tel;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class TelTest extends ParameterTest {

    @Test
    public void testValidation() {
        Tel.Builder builder = new Tel.Builder();
        prepareParameter(builder);
        Tel tel = builder.create();
        Assert.assertTrue(tel.isValid("+79876543210"));
        Assert.assertTrue(tel.isValid("89876543210"));
        Assert.assertTrue(tel.isValid("+7 (987) 654-32-10"));
        Assert.assertFalse(tel.isValid("no tel"));

        testEmptyValues(builder);
    }
}
