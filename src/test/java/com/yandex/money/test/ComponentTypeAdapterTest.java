package com.yandex.money.test;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.yandex.money.api.model.showcase.components.Component;
import com.yandex.money.api.typeadapters.showcase.container.GroupTypeAdapter;
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
