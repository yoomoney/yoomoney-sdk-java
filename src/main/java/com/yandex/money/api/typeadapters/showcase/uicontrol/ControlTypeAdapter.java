package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.methods.JsonUtils;
import com.yandex.money.api.model.showcase.components.uicontrol.Control;

/**
 * Base type adapter for components implementing {@link Control} interface.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
abstract class ControlTypeAdapter<T extends Control, U extends Control.Builder>
        extends ComponentTypeAdapter<T, U> {

    private static final String MEMBER_ALERT = "alert";
    private static final String MEMBER_HINT = "hint";
    private static final String MEMBER_LABEL = "label";
    private static final String MEMBER_READONLY = "readonly";
    private static final String MEMBER_REQUIRED = "required";

    @Override
    protected void deserialize(JsonObject src, U builder, JsonDeserializationContext context) {
        builder.setAlert(JsonUtils.getString(src, MEMBER_ALERT));
        builder.setHint(JsonUtils.getString(src, MEMBER_HINT));
        builder.setLabel(JsonUtils.getString(src, MEMBER_LABEL));
        if (src.has(MEMBER_READONLY)) {
            builder.setReadonly(src.get(MEMBER_READONLY).getAsBoolean());
        }
        if (src.has(MEMBER_REQUIRED)) {
            builder.setRequired(src.get(MEMBER_REQUIRED).getAsBoolean());
        }
    }

    @Override
    protected void serialize(T src, JsonObject to, JsonSerializationContext context) {
        to.addProperty(MEMBER_ALERT, src.alert);
        to.addProperty(MEMBER_HINT, src.hint);
        to.addProperty(MEMBER_LABEL, src.label);
        if (src.readonly) {
            to.addProperty(MEMBER_READONLY, true);
        }

        if (!src.required) {
            to.addProperty(MEMBER_REQUIRED, false);
        }
    }
}
