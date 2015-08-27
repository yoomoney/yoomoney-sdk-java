package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.methods.JsonUtils;
import com.yandex.money.api.model.showcase.components.Component;
import com.yandex.money.api.model.showcase.components.container.Group;
import com.yandex.money.api.model.showcase.components.uicontrol.Select;
import com.yandex.money.api.typeadapters.showcase.ComponentsTypeProvider;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class SelectTypeAdapter extends ParameterControlTypeAdapter<Select, Select.Builder> {

    private static final String KEY_OPTIONS = "options";
    private static final String KEY_LABEL = "label";
    private static final String KEY_VALUE = "value";
    private static final String KEY_STYLE = "style";
    private static final String KEY_GROUP = "group";

    @Override
    protected void deserialize(JsonObject from, Select.Builder builder,
                               JsonDeserializationContext context) {
        for (JsonElement item : from.getAsJsonArray(KEY_OPTIONS)) {
            JsonObject itemObject = item.getAsJsonObject();

            Select.Option option = new Select.Option(itemObject.get(KEY_LABEL).getAsString(),
                    itemObject.get(KEY_VALUE).getAsString());
            if (itemObject.has(KEY_GROUP)) {
                option.group = deserializeOptionGroup(itemObject.get(KEY_GROUP).getAsJsonArray(),
                        context);
            }
            builder.addOption(option);
        }
        builder.setStyle(Select.Style.parse(JsonUtils.getString(from, KEY_STYLE)));
        super.deserialize(from, builder, context);
    }

    @Override
    protected void serialize(Select from, JsonObject to, JsonSerializationContext context) {
        Select.Option selectedOption = from.getSelectedOption();

        if (selectedOption != null) {
            to.addProperty(KEY_VALUE, selectedOption.value);
        }
        if (from.style != null) {
            to.addProperty(KEY_STYLE, from.style.code);
        }

        JsonArray options = new JsonArray();
        for (Select.Option option : from.options) {
            JsonObject optionElement = new JsonObject();

            optionElement.addProperty(KEY_LABEL, option.label);
            optionElement.addProperty(KEY_VALUE, option.value);

            if (option.group != null) {
                optionElement.add(KEY_GROUP, serializeOptionGroup(option.group, context));
            }
            options.add(optionElement);
        }
        to.add(KEY_OPTIONS, options);
        super.serialize(from, to, context);
    }

    @Override
    protected Select.Builder createBuilderInstance() {
        return new Select.Builder();
    }

    @Override
    protected Select createInstance(Select.Builder builder) {
        return builder.create();
    }

    private static JsonArray serializeOptionGroup(Group group, JsonSerializationContext context) {
        JsonArray items = new JsonArray();
        for (Component item : group.items) {
            items.add(context.serialize(item));
        }
        return items;
    }

    private static Group deserializeOptionGroup(JsonArray items, JsonDeserializationContext
            context) {
        Group.Builder groupBuilder = new Group.Builder();
        groupBuilder.setLayout(Group.Layout.VERTICAL);
        for (JsonElement item : items) {
            Component component = context.deserialize(item,
                    ComponentsTypeProvider.getJsonComponentType(item));
            groupBuilder.addItem(component);
        }
        return groupBuilder.create();
    }
}
