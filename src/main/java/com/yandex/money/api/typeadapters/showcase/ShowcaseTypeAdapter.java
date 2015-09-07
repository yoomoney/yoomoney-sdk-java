package com.yandex.money.api.typeadapters.showcase;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.methods.JsonUtils;
import com.yandex.money.api.model.AllowedMoneySource;
import com.yandex.money.api.model.showcase.Showcase;
import com.yandex.money.api.model.showcase.Showcase.Error;
import com.yandex.money.api.model.showcase.components.container.Group;
import com.yandex.money.api.typeadapters.BaseTypeAdapter;
import com.yandex.money.api.typeadapters.GsonProvider;
import com.yandex.money.api.typeadapters.showcase.container.GroupTypeAdapter;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author raymank26
 */
public final class ShowcaseTypeAdapter extends BaseTypeAdapter<Showcase> {

    private static final String ELEMENT_TITLE = "title";
    private static final String ELEMENT_HIDDEN_FIELDS = "hidden_fields";
    private static final String ELEMENT_FORM = "form";
    private static final String ELEMENT_MONEY_SOURCE = "money_source";
    private static final String ELEMENT_ERROR = "error";

    @Override
    public Showcase deserialize(JsonElement json, Type typeOfT,
                                JsonDeserializationContext context) throws JsonParseException {

        JsonObject root = json.getAsJsonObject();
        String title = JsonUtils.getMandatoryString(root, ELEMENT_TITLE);

        JsonElement hiddenFieldsElement = root.get(ELEMENT_HIDDEN_FIELDS);
        Map<String, String> hiddenFields = JsonUtils.map(hiddenFieldsElement.getAsJsonObject());

        Set<AllowedMoneySource> moneySource = new HashSet<>(JsonUtils.getArray(root,
                ELEMENT_MONEY_SOURCE, AllowedMoneySourceTypeAdapter.INSTANCE));

        Group rootGroup = context.deserialize(root.getAsJsonArray(ELEMENT_FORM), Group.class);

        List<Error> fieldsErrors = JsonUtils.getArray(root, ELEMENT_ERROR,
                ShowcaseErrorTypeAdapter.INSTANCE);

        return new Showcase(title, hiddenFields, rootGroup, moneySource, fieldsErrors);
    }

    @Override
    public JsonElement serialize(Showcase src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject root = new JsonObject();

        root.addProperty(ELEMENT_TITLE, src.title);
        root.add(ELEMENT_MONEY_SOURCE, JsonUtils.toJsonArray(src.moneySources,
                AllowedMoneySourceTypeAdapter.INSTANCE));
        root.add(ELEMENT_ERROR, JsonUtils.toJsonArray(src.errors,
                ShowcaseErrorTypeAdapter.INSTANCE));
        root.add(ELEMENT_FORM, GroupTypeAdapter.INSTANCE.toJsonTree(src.form));
        root.add(ELEMENT_HIDDEN_FIELDS, GsonProvider.getGson().toJsonTree(src.hiddenFields));
        return root;
    }

    @Override
    protected Class<Showcase> getType() {
        return Showcase.class;
    }

    private static final class AllowedMoneySourceTypeAdapter
            extends BaseTypeAdapter<AllowedMoneySource> {

        public static final AllowedMoneySourceTypeAdapter INSTANCE =
                new AllowedMoneySourceTypeAdapter();

        private AllowedMoneySourceTypeAdapter() {
        }

        @Override
        public AllowedMoneySource deserialize(JsonElement json, Type typeOfT,
                                              JsonDeserializationContext context)
                throws JsonParseException {
            return AllowedMoneySource.parse(json.getAsJsonPrimitive().getAsString());
        }

        @Override
        public JsonElement serialize(AllowedMoneySource src, Type typeOfSrc,
                                     JsonSerializationContext context) {
            return new JsonPrimitive(src.code);
        }

        @Override
        protected Class<AllowedMoneySource> getType() {
            return AllowedMoneySource.class;
        }
    }

    private static final class ShowcaseErrorTypeAdapter extends BaseTypeAdapter<Showcase.Error> {

        public static final ShowcaseErrorTypeAdapter INSTANCE = new ShowcaseErrorTypeAdapter();

        private static final String MEMBER_NAME = "name";
        private static final String MEMBER_ALERT = "alert";

        @Override
        public Error deserialize(JsonElement json, Type typeOfT,
                                 JsonDeserializationContext context) throws JsonParseException {

            JsonObject jsonObject = json.getAsJsonObject();
            return new Error(JsonUtils.getMandatoryString(jsonObject, MEMBER_NAME),
                    JsonUtils.getMandatoryString(jsonObject, MEMBER_ALERT));
        }

        @Override
        public JsonElement serialize(Error src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("name", src.name);
            jsonObject.addProperty("alert", src.alert);
            return jsonObject;
        }

        @Override
        protected Class<Error> getType() {
            return Showcase.Error.class;
        }
    }
}
