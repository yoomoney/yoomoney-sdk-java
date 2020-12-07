/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 NBCO YooMoney LLC
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

package com.yoo.money.api.showcase;

import com.yoo.money.api.model.showcase.components.uicontrols.Select;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

public class SelectTest extends ParameterTest {

    @Test
    public void textValidation() {
        Select.Builder builder = createSelectBuilder();
        Select select = builder.create();
        assertTrue(select.isValid("value1"));
        assertFalse(select.isValid("value5"));
        testEmptyValues(builder);
    }

    @Test
    public void testValueSet() {
        Select select = createSelectBuilder().create();
        select.setValue("missing");
        assertNull(select.getValue());
        assertNull(select.getSelectedOption());

        String expectedValue = "value2";
        select.setValue(expectedValue);
        assertEquals(select.getValue(), expectedValue);
        assertEquals(select.getSelectedOption(), select.options.get(1));

        select.setValue(null);
        assertNull(select.getValue());
        assertNull(select.getSelectedOption());
    }

    private static Select.Builder createSelectBuilder() {
        Select.Builder builder = new Select.Builder()
                .addOption(new Select.Option("label1", "value1", null))
                .addOption(new Select.Option("label2", "value2", null))
                .addOption(new Select.Option("label3", "value3", null));
        prepareParameter(builder);
        return builder;
    }
}
