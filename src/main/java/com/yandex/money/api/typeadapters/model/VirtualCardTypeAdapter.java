package com.yandex.money.api.typeadapters.model;

import com.google.gson.*;
import com.yandex.money.api.model.VirtualCard;
import com.yandex.money.api.typeadapters.BaseTypeAdapter;

import java.lang.reflect.Type;

/**
 * Type adapter for {@link VirtualCard} class.
 */
import static com.yandex.money.api.typeadapters.JsonUtils.getString;

public class VirtualCardTypeAdapter extends BaseTypeAdapter<VirtualCard> {

    private static final VirtualCardTypeAdapter INSTANCE = new VirtualCardTypeAdapter();

    private static final String MEMBER_STATE = "state";

    private VirtualCardTypeAdapter() {}

    /**
     * @return instance of this class
     */
    public static VirtualCardTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    public VirtualCard deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject object = json.getAsJsonObject();
        VirtualCard.Builder builder = new VirtualCard.Builder();
        builder.setState(VirtualCard.State.parse(getString(object, MEMBER_STATE)));
        CardTypeAdapter.Delegate.deserialize(object, builder);
        return builder.create();
    }

    @Override
    public JsonElement serialize(VirtualCard src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty(MEMBER_STATE, src.state.code);
        CardTypeAdapter.Delegate.serialize(object, src);
        return object;
    }

    @Override
    protected Class<VirtualCard> getType() {
        return VirtualCard.class;
    }
}
