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

import com.google.gson.Gson;
import com.yandex.money.api.model.AllowedMoneySource;
import com.yandex.money.api.model.showcase.Showcase;
import com.yandex.money.api.typeadapters.TypeAdapter;
import com.yandex.money.api.typeadapters.model.showcase.ShowcaseTypeAdapter;
import com.yandex.money.api.typeadapters.model.showcase.container.GroupTypeAdapter;
import com.yandex.money.api.typeadapters.model.showcase.container.ParagraphTypeAdapter;
import com.yandex.money.api.typeadapters.model.showcase.uicontrol.AmountTypeAdapter;
import com.yandex.money.api.typeadapters.model.showcase.uicontrol.CheckboxTypeAdapter;
import com.yandex.money.api.typeadapters.model.showcase.uicontrol.DateTypeAdapter;
import com.yandex.money.api.typeadapters.model.showcase.uicontrol.EmailTypeAdapter;
import com.yandex.money.api.typeadapters.model.showcase.uicontrol.MonthTypeAdapter;
import com.yandex.money.api.typeadapters.model.showcase.uicontrol.NumberTypeAdapter;
import com.yandex.money.api.typeadapters.model.showcase.uicontrol.SelectTypeAdapter;
import com.yandex.money.api.typeadapters.model.showcase.uicontrol.SubmitTypeAdapter;
import com.yandex.money.api.typeadapters.model.showcase.uicontrol.TelTypeAdapter;
import com.yandex.money.api.typeadapters.model.showcase.uicontrol.TextAreaTypeAdapter;
import com.yandex.money.api.typeadapters.model.showcase.uicontrol.TextTypeAdapter;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

/**
 * Checks {@link com.yandex.money.api.model.showcase.components.Component}'s {@link Gson} type
 * adapters.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class ShowcaseTypeAdapterTest {

    @Test
    public void testCheckbox() {
        check("checkbox.json", CheckboxTypeAdapter.getInstance());
    }

    @Test
    public void testText() {
        check("text.json", TextTypeAdapter.getInstance());
    }

    @Test
    public void testTextArea() {
        check("textarea.json", TextAreaTypeAdapter.getInstance());
    }

    @Test
    public void testEmail() {
        check("email.json", EmailTypeAdapter.getInstance());
    }

    @Test
    public void testDate() {
        check("date.json", DateTypeAdapter.getInstance());
    }

    @Test
    public void testDateBoundaries() {
        check("date_boundaries.json", DateTypeAdapter.getInstance());
    }

    @Test
    public void testMonth() {
        check("month.json", MonthTypeAdapter.getInstance());
    }

    @Test
    public void testSelectNoGroup() {
        check("select_nogroup.json", SelectTypeAdapter.getInstance());
    }

    @Test
    public void testNumber() {
        check("number.json", NumberTypeAdapter.getInstance());
    }

    @Test
    public void testSelect() {
        check("select_group.json", SelectTypeAdapter.getInstance());
    }

    @Test
    public void testTel() {
        check("tel.json", TelTypeAdapter.getInstance());
    }

    @Test
    public void testSubmit() {
        check("submit.json", SubmitTypeAdapter.getInstance());
    }

    @Test
    public void testParagraph() {
        check("paragraph.json", ParagraphTypeAdapter.getInstance());
    }

    @Test
    public void testGroup() {
        check("group.json", GroupTypeAdapter.getInstance());
    }

    @Test
    public void testAmountStdFee() {
        check("amount_stdfee.json", AmountTypeAdapter.getInstance());
    }

    @Test
    public void testAmountCustomFee() {
        check("amount_customfee.json", AmountTypeAdapter.getInstance());
    }

    @Test
    public void testShowcaseBills() {
        testShowcase("showcase_bills.json");
    }

    @Test
    public void testShowcaseSkype() {
        testShowcase("showcase_skype.json");
    }

    private static void testShowcase(String fileName) {
        ShowcaseTypeAdapter adapter = ShowcaseTypeAdapter.getInstance();
        check(fileName, adapter);

        try {
            Showcase showcase = adapter.fromJson(Resources.load(getPath(fileName)));
            Object allowedMoneySource = showcase.moneySources.get(0);
            assertEquals(allowedMoneySource.getClass(), AllowedMoneySource.class);
        } catch (FileNotFoundException e) {
            fail();
        }
    }

    private static <T> void check(String fileName, TypeAdapter<T> typeAdapter) {
        ModelTests.checkTypeAdapter(getPath(fileName), typeAdapter);
    }

    private static String getPath(String fileName) {
        return "/showcase/" + fileName;
    }
}
