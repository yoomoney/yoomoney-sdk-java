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

    private static final Map<Component.Type, Type> TYPE_MAPPING;

    private static final Map<Type, String> REVERSE_TYPE_MAPPING;

    static {
        Map<Component.Type, Type> typeMapping = new HashMap<>();

        typeMapping.put(Component.Type.AMOUNT, Amount.class);
        typeMapping.put(Component.Type.CHECKBOX, Checkbox.class);
        typeMapping.put(Component.Type.DATE, Date.class);
        typeMapping.put(Component.Type.EMAIL, Email.class);
        typeMapping.put(Component.Type.GROUP, Group.class);
        typeMapping.put(Component.Type.MONTH, Month.class);
        typeMapping.put(Component.Type.NUMBER, Number.class);
        typeMapping.put(Component.Type.PARAGRAPH, Paragraph.class);
        typeMapping.put(Component.Type.SELECT, Select.class);
        typeMapping.put(Component.Type.SUBMIT, Submit.class);
        typeMapping.put(Component.Type.TEL, Tel.class);
        typeMapping.put(Component.Type.TEXT, Text.class);
        typeMapping.put(Component.Type.TEXT_AREA, TextArea.class);

        TYPE_MAPPING = Collections.unmodifiableMap(typeMapping);

        Map<Type, String> reverseTypeMapping = new HashMap<>();

        for (HashMap.Entry<Component.Type, Type> item : TYPE_MAPPING.entrySet()) {
            reverseTypeMapping.put(item.getValue(), item.getKey().code);
        }

        REVERSE_TYPE_MAPPING = Collections.unmodifiableMap(reverseTypeMapping);
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
