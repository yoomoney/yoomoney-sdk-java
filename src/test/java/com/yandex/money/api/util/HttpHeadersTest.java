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

package com.yandex.money.api.util;

import com.yandex.money.api.time.DateTime;
import org.testng.annotations.Test;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static org.testng.Assert.assertEquals;

public class HttpHeadersTest {

    private final Map<String, DateTime> testData = new HashMap<>(2); {
        DateTime value = DateTime.from(1994, 10, 15, 8, 12)
                .withZone(TimeZone.getTimeZone("GMT"));
        testData.put("Tue, 15 Nov 1994 08:12:00 GMT", value);

        value = DateTime.from(1994, 10, 15, 8, 12)
                .withZone(TimeZone.getTimeZone("Europe/Moscow"));
        testData.put("Tue, 15 Nov 1994 08:12:00 MSK", value);
    }

    @Test
    public void testParser() throws ParseException {
        for (Map.Entry<String, DateTime> entry : testData.entrySet()) {
            assertEquals(HttpHeaders.parseDateTime(entry.getKey()).getDate(), entry.getValue().getDate());
        }
    }

    @Test
    public void testFormatter() {
        for (Map.Entry<String, DateTime> entry : testData.entrySet()) {
            assertEquals(HttpHeaders.formatDateTime(entry.getValue()), entry.getKey());
        }
    }
}
