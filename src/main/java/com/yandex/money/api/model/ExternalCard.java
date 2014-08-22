package com.yandex.money.api.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.api.methods.JsonUtils;
import com.yandex.money.api.utils.Strings;

import java.lang.reflect.Type;

/**
 * Represents card that not bound to an account.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class ExternalCard extends Card {

    private final String fundingSourceType;
    private final String moneySourceToken;

    /**
     * Constructor.
     *
     * @param panFragment panned fragment of card's number
     * @param type type of a card
     */
    public ExternalCard(String panFragment, String type, String fundingSourceType,
                        String moneySourceToken) {

        super(null, panFragment, type);
        if (Strings.isNullOrEmpty(fundingSourceType)) {
            throw new NullPointerException("fundingSourceType is null or empty");
        }
        if (Strings.isNullOrEmpty(moneySourceToken)) {
            throw new NullPointerException("money source token is null or empty");
        }
        this.fundingSourceType = fundingSourceType;
        this.moneySourceToken = moneySourceToken;
    }

    public static ExternalCard createFromJson(JsonElement jsonElement) {
        return buildGson().fromJson(jsonElement, ExternalCard.class);
    }

    public String getFundingSourceType() {
        return fundingSourceType;
    }

    public String getMoneySourceToken() {
        return moneySourceToken;
    }

    private static Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(ExternalCard.class, new Deserializer())
                .create();
    }

    private static final class Deserializer implements JsonDeserializer<ExternalCard> {
        @Override
        public ExternalCard deserialize(JsonElement json, Type typeOfT,
                                        JsonDeserializationContext context)
                throws JsonParseException {

            JsonObject object = json.getAsJsonObject();
            return new ExternalCard(JsonUtils.getMandatoryString(object, "pan_fragment"),
                    JsonUtils.getMandatoryString(object, "payment_card_type"),
                    JsonUtils.getMandatoryString(object, "type"),
                    JsonUtils.getMandatoryString(object, "money_source_token"));
        }
    }
}
