package com.yandex.money.api.typeadapters.showcase.container;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
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
public final class GroupTypeAdapter extends ContainerTypeAdapter<Component, Group, Group.Builder> {

    private static final HashMap<Component.Type, Type> mapping = new HashMap<>();

    static {
        mapping.put(Component.Type.AMOUNT, Amount.class);
        mapping.put(Component.Type.CHECKBOX, Checkbox.class);
        mapping.put(Component.Type.DATE, Date.class);
        mapping.put(Component.Type.EMAIL, Email.class);
        mapping.put(Component.Type.GROUP, Group.class);
        mapping.put(Component.Type.MONTH, Month.class);
        mapping.put(Component.Type.NUMBER, Number.class);
        mapping.put(Component.Type.PARAGRAPH, Paragraph.class);
        mapping.put(Component.Type.SELECT, Select.class);
        mapping.put(Component.Type.SUBMIT, Submit.class);
        mapping.put(Component.Type.TEL, Tel.class);
        mapping.put(Component.Type.TEXT, Text.class);
        mapping.put(Component.Type.TEXT_AREA, TextArea.class);
    }

    @Override
    protected Group.Builder createBuilderInstance() {
        return new Group.Builder();
    }

    @Override
    protected Group createInstance(Group.Builder builder) {
        return builder.create();
    }

    @Override
    protected JsonElement serializeItem(Component item, JsonSerializationContext context) {
        return context.serialize(item, mapping.get(item.type));
    }

    @Override
    protected Component deserializeItem(JsonElement json, JsonDeserializationContext context) {
        Component.Type type = Component.Type.parse(json.getAsJsonObject().get("type")
                .getAsString());
        return context.deserialize(json, mapping.get(type));
    }
}
