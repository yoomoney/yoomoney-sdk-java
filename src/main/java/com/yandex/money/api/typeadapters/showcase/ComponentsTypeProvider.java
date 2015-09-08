package com.yandex.money.api.typeadapters.showcase;

import com.google.gson.JsonElement;
import com.yandex.money.api.model.showcase.components.Component;
import com.yandex.money.api.model.showcase.components.container.Group;
import com.yandex.money.api.model.showcase.components.container.Paragraph;
import com.yandex.money.api.model.showcase.components.uicontrol.Amount;
import com.yandex.money.api.model.showcase.components.uicontrol.Checkbox;
import com.yandex.money.api.model.showcase.components.uicontrol.Date;
import com.yandex.money.api.model.showcase.components.uicontrol.Email;
import com.yandex.money.api.model.showcase.components.uicontrol.Month;
import com.yandex.money.api.model.showcase.components.uicontrol.Number;
import com.yandex.money.api.model.showcase.components.uicontrol.Select;
import com.yandex.money.api.model.showcase.components.uicontrol.Submit;
import com.yandex.money.api.model.showcase.components.uicontrol.Tel;
import com.yandex.money.api.model.showcase.components.uicontrol.Text;
import com.yandex.money.api.model.showcase.components.uicontrol.TextArea;
import com.yandex.money.api.typeadapters.showcase.uicontrol.ComponentTypeAdapter;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class ComponentsTypeProvider {

    private static Map<Component.Type, Type> TYPE_MAPPING = new HashMap<>();

    private static Map<Type, String> REVERSE_TYPE_MAPPING = new HashMap<>();

    static {
        TYPE_MAPPING.put(Component.Type.AMOUNT, Amount.class);
        TYPE_MAPPING.put(Component.Type.CHECKBOX, Checkbox.class);
        TYPE_MAPPING.put(Component.Type.DATE, Date.class);
        TYPE_MAPPING.put(Component.Type.EMAIL, Email.class);
        TYPE_MAPPING.put(Component.Type.GROUP, Group.class);
        TYPE_MAPPING.put(Component.Type.MONTH, Month.class);
        TYPE_MAPPING.put(Component.Type.NUMBER, Number.class);
        TYPE_MAPPING.put(Component.Type.PARAGRAPH, Paragraph.class);
        TYPE_MAPPING.put(Component.Type.SELECT, Select.class);
        TYPE_MAPPING.put(Component.Type.SUBMIT, Submit.class);
        TYPE_MAPPING.put(Component.Type.TEL, Tel.class);
        TYPE_MAPPING.put(Component.Type.TEXT, Text.class);
        TYPE_MAPPING.put(Component.Type.TEXT_AREA, TextArea.class);

        TYPE_MAPPING = Collections.unmodifiableMap(TYPE_MAPPING);

        for (HashMap.Entry<Component.Type, Type> item : TYPE_MAPPING.entrySet()) {
            REVERSE_TYPE_MAPPING.put(item.getValue(), item.getKey().code);
        }
        REVERSE_TYPE_MAPPING = Collections.unmodifiableMap(REVERSE_TYPE_MAPPING);
    }

    private ComponentsTypeProvider() {
    }

    public static Type getJsonComponentType(JsonElement component) {
        return TYPE_MAPPING.get(Component.Type.parse(component.getAsJsonObject().get(
                ComponentTypeAdapter.MEMBER_TYPE).getAsString()));
    }

    public static String getTypeFromClass(Type type) {
        return REVERSE_TYPE_MAPPING.get(type);
    }
}