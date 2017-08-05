/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 NBCO Yandex.Money LLC
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

import com.yandex.money.api.Resources;
import com.yandex.money.api.model.showcase.Showcase;
import com.yandex.money.api.typeadapters.model.showcase.ShowcaseTypeAdapter;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class ShowcaseTest {

    @Test
    public void testShowcaseParameters() throws FileNotFoundException {
        Showcase showcase = ShowcaseTypeAdapter.getInstance().fromJson(Resources.load("/model/showcase.json"));
        assertNotNull(showcase);

        Map<String, String> parameters = showcase.getPaymentParameters();
        assertNotNull(parameters);
        assertFalse(parameters.isEmpty());

        assertNotNull(showcase.hiddenFields);
        assertFalse(showcase.hiddenFields.isEmpty());

        for (Map.Entry<String, String> field : showcase.hiddenFields.entrySet()) {
            String key = field.getKey();
            String value = parameters.get(key);
            assertNotNull(value);
            assertTrue(value.equals(field.getValue()));
        }
    }
}
