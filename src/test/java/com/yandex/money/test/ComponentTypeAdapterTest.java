package com.yandex.money.test;

import com.google.gson.JsonObject;
import com.yandex.money.api.model.showcase.components.Parameter;
import com.yandex.money.api.model.showcase.components.uicontrol.Checkbox;
import com.yandex.money.api.model.showcase.components.uicontrol.ParameterControl;
import com.yandex.money.api.typeadapters.showcase.uicontrol.helpers.CheckboxTypeTypeAdapter;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class ComponentTypeAdapterTest {

    private static final String KEY_ALERT = "alert";
    private static final String KEY_HINT = "hint";
    private static final String KEY_LABEL = "label";
    private static final String KEY_READONLY = "readonly";
    private static final String KEY_REQUIRED = "required";
    private static final String KEY_VALUE = "value";
    private static final String KEY_AUTOFILL = "value_autofill";

    private static final String KEY_CHECKED = "checked";

    private static final String VALUE_ALERT = "alert value";
    private static final String VALUE_HINT = "hint value";
    private static final String VALUE_LABEL = "label value";
    private static final String VALUE = "value";
    private static final Parameter.AutoFill VALUE_AUTOFILL = Parameter.AutoFill.CURRENT_USER_EMAIL;
    private static final boolean VALUE_READONLY = true;
    private static final boolean VALUE_REQUIRED = false;

    private static final boolean VALUE_CHECKED = true;

    @Test
    public void testCheckbox() {

        Checkbox.Builder checkboxBuilder = new Checkbox.Builder()
                .setChecked(VALUE_CHECKED);

        Checkbox checkbox = populateBuilder(new Checkbox.Builder().setChecked(VALUE_CHECKED))
                .create();

        CheckboxTypeTypeAdapter checkboxTypeAdapter = new CheckboxTypeTypeAdapter();

        JsonObject jsonElement = checkboxTypeAdapter.toJsonTree(checkbox).getAsJsonObject();
        assertParameterControl(jsonElement);

        Assert.assertEquals(jsonElement.get(KEY_CHECKED).getAsBoolean(), VALUE_CHECKED);
    }

    @SuppressWarnings("unchecked")
    private <T extends ParameterControl.Builder> T populateBuilder(T builder) {
        return (T) builder.setName("some_name")
                .setValue(VALUE)
                .setValueAutoFill(VALUE_AUTOFILL)
                .setAlert(VALUE_ALERT)
                .setHint(VALUE_HINT)
                .setLabel(VALUE_LABEL)
                .setReadonly(VALUE_READONLY)
                .setRequired(VALUE_REQUIRED);
    }

    private void assertParameterControl(JsonObject jsonElement) {
        Assert.assertEquals(jsonElement.get(KEY_ALERT).getAsString(), VALUE_ALERT);
        Assert.assertEquals(jsonElement.get(KEY_HINT).getAsString(), VALUE_HINT);
        Assert.assertEquals(jsonElement.get(KEY_LABEL).getAsString(), VALUE_LABEL);
        Assert.assertEquals(jsonElement.get(KEY_VALUE).getAsString(), VALUE);
        Assert.assertEquals(jsonElement.get(KEY_AUTOFILL).getAsString(), VALUE_AUTOFILL.code);
        Assert.assertEquals(jsonElement.get(KEY_REQUIRED).getAsBoolean(), VALUE_REQUIRED);
        Assert.assertEquals(jsonElement.get(KEY_READONLY).getAsBoolean(), VALUE_READONLY);
    }
}
