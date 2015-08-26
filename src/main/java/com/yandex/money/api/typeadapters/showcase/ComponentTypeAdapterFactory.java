package com.yandex.money.api.typeadapters.showcase;

import com.yandex.money.api.model.showcase.components.Component;
import com.yandex.money.api.model.showcase.components.container.Group;
import com.yandex.money.api.model.showcase.components.container.Paragraph;
import com.yandex.money.api.model.showcase.components.uicontrol.Amount;
import com.yandex.money.api.model.showcase.components.uicontrol.Checkbox;
import com.yandex.money.api.model.showcase.components.uicontrol.Date;
import com.yandex.money.api.model.showcase.components.uicontrol.Email;
import com.yandex.money.api.model.showcase.components.uicontrol.Month;
import com.yandex.money.api.model.showcase.components.uicontrol.Select;
import com.yandex.money.api.model.showcase.components.uicontrol.Submit;
import com.yandex.money.api.model.showcase.components.uicontrol.Tel;
import com.yandex.money.api.model.showcase.components.uicontrol.Text;
import com.yandex.money.api.model.showcase.components.uicontrol.TextArea;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public class ComponentTypeAdapterFactory {

    private static final HashMap<Component.Type, Type> TYPE_MAPPING = new HashMap<>();

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
    }

    public static Type getClassFromType(String code) {
        return TYPE_MAPPING.get(Component.Type.parse(code));
    }

}
