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

package com.yandex.money.api.showcase;

import com.yandex.money.api.ApiTest;
import com.yandex.money.api.model.AllowedMoneySource;
import com.yandex.money.api.model.showcase.AmountType;
import com.yandex.money.api.model.showcase.Fee;
import com.yandex.money.api.model.showcase.Showcase;
import com.yandex.money.api.model.showcase.ShowcaseContext;
import com.yandex.money.api.model.showcase.StdFee;
import com.yandex.money.api.model.showcase.components.Component;
import com.yandex.money.api.model.showcase.components.Parameter;
import com.yandex.money.api.model.showcase.components.TextBlock;
import com.yandex.money.api.model.showcase.components.containers.Group;
import com.yandex.money.api.model.showcase.components.containers.Paragraph;
import com.yandex.money.api.model.showcase.components.uicontrols.Amount;
import com.yandex.money.api.model.showcase.components.uicontrols.Checkbox;
import com.yandex.money.api.model.showcase.components.uicontrols.Date;
import com.yandex.money.api.model.showcase.components.uicontrols.Email;
import com.yandex.money.api.model.showcase.components.uicontrols.Month;
import com.yandex.money.api.model.showcase.components.uicontrols.Number;
import com.yandex.money.api.model.showcase.components.uicontrols.ParameterControl;
import com.yandex.money.api.model.showcase.components.uicontrols.Select;
import com.yandex.money.api.model.showcase.components.uicontrols.Submit;
import com.yandex.money.api.model.showcase.components.uicontrols.Tel;
import com.yandex.money.api.model.showcase.components.uicontrols.Text;
import com.yandex.money.api.model.showcase.components.uicontrols.TextArea;
import com.yandex.money.api.net.ApiClient;
import com.yandex.money.api.typeadapters.model.showcase.ShowcaseTypeAdapter;
import com.yandex.money.api.util.Currency;
import org.joda.time.DateTime;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class ShowcaseParserTest {

    @Test
    public void testParsing() throws Exception {
        ApiClient client = ApiTest.DEFAULT_API_CLIENT_BUILDER.create();
        testShowcase(client, 5551L);
    }

    @Test
    public void testResourceShowcase() {
        InputStream inputStream = ShowcaseParserTest.class.getResourceAsStream("/showcase.json");
        if (inputStream == null) {
            return;
        }
        Showcase showcase = ShowcaseTypeAdapter.getInstance().fromJson(inputStream);
        checkShowcase(showcase);
    }

    @Test
    public void testCustomShowcase() {
        final String SHOWCASE = "{\"title\":\"Квитанции\",\"form\":[{\"type\":\"group\"," +
                "\"layout\":\"VBox\",\"items\":[{\"type\":\"text\",\"name\":\"supplierInn\"," +
                "\"value\":\"525536353633\",\"hint\":\"10 цифр (для ИП - 12 цифр)\"," +
                "\"label\":\"ИНН получателя:\",\"alert\":\"Не заполнено поле «ИНН получателя»\"," +
                "\"required\":true,\"readonly\":false,\"minlength\":10,\"maxlength\":12," +
                "\"pattern\":\"^\\\\d{10}$|^\\\\d{12}$\"},{\"type\":\"text\"," +
                "\"name\":\"supplierBankAccount\",\"hint\":\"20 цифр\",\"label\":\"Расчетный " +
                "счет\",\"alert\":\"Не заполнено поле «Расчетный счет получателя»\"," +
                "\"required\":true,\"readonly\":false,\"minlength\":20,\"maxlength\":20}," +
                "{\"type\":\"text\",\"name\":\"supplierName\",\"hint\":\"Включая " +
                "организационно-правовую форму: ООО, ИП и т.д.\",\"label\":\"Название " +
                "организации\",\"alert\":\"Не заполнено поле «Наименование получателя»\"," +
                "\"required\":true,\"readonly\":false,\"minlength\":1,\"maxlength\":200}]}," +
                "{\"type\":\"group\",\"layout\":\"VBox\",\"items\":[{\"type\":\"text\"," +
                "\"name\":\"supplierBankBik\",\"hint\":\"9 цифр\",\"label\":\"БИК банка " +
                "получателя:\",\"alert\":\"Не заполнено поле «БИК банка получателя»\"," +
                "\"required\":true,\"readonly\":false,\"minlength\":9,\"maxlength\":9}]}," +
                "{\"type\":\"group\",\"layout\":\"VBox\",\"items\":[{\"type\":\"text\"," +
                "\"name\":\"paymentKbk\",\"hint\":\"20 цифр\",\"label\":\"КБК\",\"alert\":\"Не " +
                "заполнено поле КБК\",\"required\":false,\"readonly\":false,\"minlength\":20," +
                "\"maxlength\":20},{\"type\":\"text\",\"name\":\"CustKPP\",\"hint\":\"9 цифр\"," +
                "\"label\":\"КПП\",\"alert\":\"Не заполнено поле КПП\",\"required\":false," +
                "\"readonly\":false,\"minlength\":9,\"maxlength\":9," +
                "\"pattern\":\"^\\\\d{4}[\\\\dA-Za-z]{2}\\\\d{3}$\"},{\"type\":\"text\"," +
                "\"name\":\"paymentOkato\",\"hint\":\"8 или 11 цифр\",\"label\":\"ОКТМО или " +
                "ОКАТО\",\"alert\":\"Не заполнено поле ОКТМО или ОКАТО\",\"required\":false," +
                "\"readonly\":false,\"minlength\":8,\"maxlength\":11," +
                "\"pattern\":\"^\\\\d{8}|\\\\d{11}$\"}]},{\"type\":\"submit\"," +
                "\"label\":\"Продолжить\"}],\"money_source\":[\"wallet\",\"cards\"," +
                "\"payment-card\",\"cash\"],\"hidden_fields\":{\"budgetDocNumber\":\"0\"," +
                "\"ContractTemplateID\":\"524867\",\"sc_param_scid\":\"5551\"," +
                "\"ShopArticleID\":\"35241\",\"has_external_status\":\"\",\"is_withdrawal\":\"\"," +
                "\"sc_param_step\":\"step_INN_3038\",\"ShopID\":\"13423\"," +
                "\"ShowCaseID\":\"3005\",\"supplierInn\":\"525536353633\"}}";
        InputStream inputStream = new ByteArrayInputStream(SHOWCASE.getBytes(Charset.forName(
                "UTF-8")));
        ShowcaseTypeAdapter.getInstance().fromJson(inputStream);
    }

    private void testShowcase(ApiClient client, long scid) throws Exception {
        ShowcaseContext showcaseContext = client.execute(new Showcase.Request(scid));
        assertNotNull(showcaseContext);
        assertNotNull(showcaseContext.getCurrentStep().showcase);
    }

    private void checkShowcase(Showcase showcase) {
        assertEquals(showcase.title, "Test Form");
        checkForm(showcase.form);
        checkMoneySources(showcase.moneySources);
        checkHiddenFields(showcase.hiddenFields);
        checkErrors(showcase.errors);
    }

    private void checkForm(Group form) {
        assertNotNull(form);
        assertEquals(form.layout, Group.Layout.VERTICAL);

        List<Component> components = form.items;
        assertEquals(components.size(), 13);

        Component component = components.get(0);
        assertTrue(component instanceof Text);
        Text text = (Text) component;
        assertEquals(text.valueAutoFill, Parameter.AutoFill.CURRENT_USER_ACCOUNT);
        checkComponentFields(text, "name1", "required");
        assertEquals(text.minLength, Integer.valueOf(2));
        assertEquals(text.maxLength, Integer.valueOf(10));
        assertEquals(text.pattern, "\\D*?");

        component = components.get(1);
        assertTrue(component instanceof Number);
        Number number = (Number) component;
        checkComponentFields(number, "name2", "10.00");
        checkNumberField(number);

        component = components.get(2);
        assertTrue(component instanceof Amount);
        Amount amount = (Amount) component;
        checkComponentFields(amount, "name3", "10.00");
        checkNumberField(amount);
        assertEquals(amount.currency, Currency.RUB);
        checkFee(amount.fee);

        component = components.get(3);
        assertTrue(component instanceof Email);
        checkComponentFields(component, "name4", "vyasevich@yamoney.ru");

        component = components.get(4);
        assertTrue(component instanceof Tel);
        checkComponentFields(component, "name5", "79876543210");

        component = components.get(5);
        assertTrue(component instanceof Checkbox);
        Checkbox checkbox = (Checkbox) component;
        checkComponentFields(component, "name6", "true");
        assertTrue(checkbox.checked);

        component = components.get(6);
        assertTrue(component instanceof Date);
        Date date = (Date) component;
        checkComponentFields(date, "name7", "2005-01-01");
        checkDate(date);

        component = components.get(7);
        assertTrue(component instanceof Month);
        Month month = (Month) component;
        checkComponentFields(month, "name8", "2005-01");
        checkDate(month);

        component = components.get(8);
        assertTrue(component instanceof Select);
        Select select = (Select) component;
        checkComponentFields(select, "name9", "value2");
        assertEquals(select.style, Select.Style.RADIO_GROUP);
        checkOptions(select.options);

        component = components.get(9);
        assertTrue(component instanceof TextArea);
        TextArea textArea = (TextArea) component;
        checkComponentFields(textArea, "name10", "value");
        assertEquals(textArea.minLength, Integer.valueOf(2));
        assertEquals(textArea.maxLength, Integer.valueOf(10));

        component = components.get(10);
        assertTrue(component instanceof Submit);
        Submit submit = (Submit) component;
        assertEquals(submit.hint, "hint");
        assertEquals(submit.label, "label");
        assertEquals(submit.alert, "alert");
        assertTrue(submit.required);
        assertFalse(submit.readonly);

        component = components.get(11);
        assertTrue(component instanceof Group);
        Group group = (Group) component;
        assertEquals(group.label, "label");
        assertEquals(group.layout, Group.Layout.HORIZONTAL);
        assertNotNull(group.items);
        assertEquals(group.items.size(), 2);

        component = components.get(12);
        assertTrue(component instanceof Paragraph);
        Paragraph paragraph = (Paragraph) component;
        assertEquals(paragraph.label, "label");

        List<TextBlock> items = paragraph.items;
        assertNotNull(items);
        assertEquals(items.size(), 3);

        TextBlock block = items.get(0);
        assertEquals(block.text, "Text with ");

        block = items.get(1);
        assertEquals(block.text, "link");
        assertTrue(block instanceof TextBlock.WithLink);
        TextBlock.WithLink link = (TextBlock.WithLink) block;
        assertEquals(link.link, "https://money.yandex.ru");

        block = items.get(2);
        assertEquals(block.text, ".");
    }

    private void checkComponentFields(Component component, String name, String value) {
        assertTrue(component instanceof ParameterControl);
        ParameterControl control = (ParameterControl) component;
        assertEquals(control.name, name);
        assertEquals(control.getValue(), value);
        assertEquals(control.hint, "hint");
        assertEquals(control.label, "label");
        assertEquals(control.alert, "alert");
        assertTrue(control.required);
        assertFalse(control.readonly);
    }

    private void checkNumberField(Number number) {
        assertEquals(number.min, new BigDecimal("5.00"));
        assertEquals(number.max, new BigDecimal("100.00"));
        assertEquals(number.step, new BigDecimal("5.00"));
    }

    private void checkFee(Fee fee) {
        assertTrue(fee instanceof StdFee);
        StdFee stdFee = (StdFee) fee;
        assertEquals(stdFee.a, new BigDecimal("0.02"));
        assertEquals(stdFee.b, new BigDecimal("10.00"));
        assertEquals(stdFee.c, new BigDecimal("10.00"));
        assertEquals(stdFee.d, new BigDecimal("15.00"));
        assertEquals(stdFee.getAmountType(), AmountType.AMOUNT);
    }

    private void checkDate(Date date) {
        assertEquals(date.min, new DateTime(2000, 1, 1, 0, 0));
        assertEquals(date.max, new DateTime(2010, 1, 1, 0, 0));
    }

    private void checkOptions(List<Select.Option> options) {
        assertEquals(options.size(), 3);

        Select.Option option = options.get(0);
        assertEquals(option.label, "label1");
        assertEquals(option.value, "value1");
        assertNull(option.group);

        option = options.get(1);
        assertEquals(option.label, "label2");
        assertEquals(option.value, "value2");
        assertNull(option.group);

        option = options.get(2);
        assertEquals(option.label, "label3");
        assertEquals(option.value, "value3");

        Group group = option.group;
        assertNotNull(group);

        List<Component> items = group.items;
        assertEquals(items.size(), 1);

        Component component = items.get(0);
        assertTrue(component instanceof Text);
        ParameterControl control = (ParameterControl) component;
        assertEquals(control.name, "name14");
        assertEquals(control.getValue(), "readonly");
        assertEquals(control.hint, "hint");
        assertEquals(control.label, "label");
        assertEquals(control.alert, "alert");
        assertFalse(control.required);
        assertTrue(control.readonly);
    }

    private void checkMoneySources(List<AllowedMoneySource> moneySources) {
        assertNotNull(moneySources);
        for (AllowedMoneySource moneySource : AllowedMoneySource.values()) {
            assertTrue(moneySources.contains(moneySource));
        }
    }

    private void checkHiddenFields(Map<String, String> hiddenFields) {
        assertNotNull(hiddenFields);
        assertEquals(hiddenFields.size(), 2);
        assertTrue("value1".equals(hiddenFields.get("field1")));
        assertTrue("value2".equals(hiddenFields.get("field2")));
    }

    private void checkErrors(List<Showcase.Error> errors) {
        assertNotNull(errors);
        assertEquals(errors.size(), 2);
        for (Showcase.Error error : errors) {
            assertEquals("name", error.name);
            assertEquals("alert", error.alert);
        }
    }
}
