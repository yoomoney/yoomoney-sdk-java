package com.yandex.money.api.typeadapters.model.showcase.container;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.model.showcase.components.Component;
import com.yandex.money.api.model.showcase.components.containers.Expand;
import com.yandex.money.api.typeadapters.model.showcase.uicontrol.*;

import static com.yandex.money.api.typeadapters.JsonUtils.getString;
import static com.yandex.money.api.typeadapters.model.showcase.container.GroupTypeAdapter.deserializeComponent;

/**
 * Type serializer for {@link Expand} component container.
 */
public final class ExpandTypeAdapter extends ContainerTypeAdapter<Component, Expand, Expand.Builder> {

    private static final ExpandTypeAdapter INSTANCE = new ExpandTypeAdapter();

    private static final String MEMBER_LABEL_MINIMIZED = "label_minimized";
    private static final String MEMBER_LABEL_EXPANDED = "label_expanded";

    private ExpandTypeAdapter() {
        // register type adapters to GSON instance.
        NumberTypeAdapter.getInstance();
        AmountTypeAdapter.getInstance();
        CheckboxTypeAdapter.getInstance();
        DateTypeAdapter.getInstance();
        EmailTypeAdapter.getInstance();
        MonthTypeAdapter.getInstance();
        SelectTypeAdapter.getInstance();
        SubmitTypeAdapter.getInstance();
        TextAreaTypeAdapter.getInstance();
        TextTypeAdapter.getInstance();
        TelTypeAdapter.getInstance();
        ParagraphTypeAdapter.getInstance();
        AdditionalDataTypeAdapter.getInstance();
        GroupTypeAdapter.getInstance();
    }

    /**
     * @return instance of this class
     */
    public static ExpandTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    protected void deserialize(JsonObject src, Expand.Builder builder, JsonDeserializationContext context) {
        builder.setLabelMinimized(getString(src,MEMBER_LABEL_MINIMIZED));
        builder.setLabelExpanded(getString(src,MEMBER_LABEL_EXPANDED));
        super.deserialize(src, builder, context);
    }

    @Override
    protected void serialize(Expand src, JsonObject to, JsonSerializationContext context) {
        to.addProperty(MEMBER_LABEL_MINIMIZED, src.labelMinimized);
        to.addProperty(MEMBER_LABEL_EXPANDED, src.labelExpanded);
        super.serialize(src, to, context);
    }

    @Override
    protected Expand.Builder createBuilderInstance() {
        return new Expand.Builder();
    }

    @Override
    protected Expand createInstance(Expand.Builder builder) {
        return builder.create();
    }

    @Override
    protected JsonElement serializeItem(Component src, JsonSerializationContext context) {
        return context.serialize(src);
    }

    @Override
    protected Component deserializeItem(JsonElement src, JsonDeserializationContext context) {
        return deserializeComponent(src, context);
    }

    @Override
    public Class<Expand> getType() {
        return Expand.class;
    }

}
