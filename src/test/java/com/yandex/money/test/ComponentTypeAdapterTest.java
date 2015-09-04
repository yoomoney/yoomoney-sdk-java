package com.yandex.money.test;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.yandex.money.api.model.showcase.components.Component;
import com.yandex.money.api.typeadapters.showcase.container.ParagraphTypeAdapter;
import com.yandex.money.api.typeadapters.showcase.uicontrol.AmountTypeAdapter;
import com.yandex.money.api.typeadapters.showcase.uicontrol.CheckboxTypeAdapter;
import com.yandex.money.api.typeadapters.showcase.uicontrol.ComponentTypeAdapter;
import com.yandex.money.api.typeadapters.showcase.uicontrol.DateTypeAdapter;
import com.yandex.money.api.typeadapters.showcase.uicontrol.EmailTypeAdapter;
import com.yandex.money.api.typeadapters.showcase.uicontrol.MonthTypeAdapter;
import com.yandex.money.api.typeadapters.showcase.uicontrol.NumberTypeAdapter;
import com.yandex.money.api.typeadapters.showcase.uicontrol.SelectTypeAdapter;
import com.yandex.money.api.typeadapters.showcase.uicontrol.SubmitTypeAdapter;
import com.yandex.money.api.typeadapters.showcase.uicontrol.TelTypeAdapter;
import com.yandex.money.api.typeadapters.showcase.uicontrol.TextAreaTypeAdapter;
import com.yandex.money.api.typeadapters.showcase.uicontrol.TextTypeAdapter;

import org.junit.Assert;
import org.testng.annotations.Test;

import java.util.Scanner;

/**
 * Checks {@link com.yandex.money.api.model.showcase.components.Component}'s {@link Gson} type
 * adapters.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class ComponentTypeAdapterTest {

    @Test
    public void testCheckbox() {
        check("checkbox.json", CheckboxTypeAdapter.INSTANCE);
    }

    @Test
    public void testText() {
        check("text.json", TextTypeAdapter.INSTANCE);
    }

    @Test
    public void testTextArea() {
        check("textarea.json", TextAreaTypeAdapter.INSTANCE);
    }

    @Test
    public void testEmail() {
        check("email.json", EmailTypeAdapter.INSTANCE);
    }

    @Test
    public void testDate() {
        check("date.json", DateTypeAdapter.INSTANCE);
    }

    @Test
    public void testMonth() {
        check("month.json", MonthTypeAdapter.INSTANCE);
    }

    @Test
    public void testSelectNoGroup() {
        check("select_nogroup.json", SelectTypeAdapter.INSTANCE);
    }

    @Test
    public void testNumber() {
        check("number.json", NumberTypeAdapter.INSTANCE);
    }

    @Test
    public void testSelect() {
        check("select_group.json", SelectTypeAdapter.INSTANCE);
    }

    @Test
    public void testTel() {
        check("tel.json", TelTypeAdapter.INSTANCE);
    }

    @Test
    public void testSubmit() {
        check("submit.json", SubmitTypeAdapter.INSTANCE);
    }

    @Test
    public void testParagraph() {
        check("paragraph.json", ParagraphTypeAdapter.INSTANCE);
    }

    @Test
    public void testAmountStdFee() {
        check("amount_stdfee.json", AmountTypeAdapter.INSTANCE);
    }

    @Test
    public void testAmountCustomFee() {
        check("amount_customfee.json", AmountTypeAdapter.INSTANCE);
    }

    private static String loadComponentJson(String name) {
        return new Scanner(ComponentTypeAdapterTest.class
                .getResourceAsStream("/components/" + name), "UTF-8").useDelimiter("\\A").next();
    }

    /**
     * Reads JSON file and asserts it for equality after deserialization and serialization steps.
     *
     * @param jsonFileName JSON file name in resources.
     * @param typeAdapter  type adapter of {@link com.yandex.money.api.model.showcase.components
     * .Component}.
     */
    private static <T extends Component> void check(String jsonFileName,
                                                    ComponentTypeAdapter<T, ?> typeAdapter) {
        String json = loadComponentJson(jsonFileName);
        T deserializedComponent = typeAdapter.fromJson(json);
        Assert.assertEquals(new JsonParser().parse(json),
                typeAdapter.toJsonTree(deserializedComponent));
    }
}
