package com.yandex.money.model.methods.misc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.model.methods.JsonUtils;

import java.lang.reflect.Type;
import java.math.BigDecimal;

public class BalanceDetails {

    private final BigDecimal total;
    private final BigDecimal available;
    private final BigDecimal depositionPending;
    private final BigDecimal blocked;
    private final BigDecimal debt;

    public BalanceDetails(BigDecimal total, BigDecimal available, BigDecimal depositionPending,
                          BigDecimal blocked, BigDecimal debt) {

        if (total == null) {
            throw new JsonParseException("balance total is null");
        }
        this.total = total;
        if (available == null) {
            throw new JsonParseException("balance available is null");
        }
        this.available = available;
        this.depositionPending = depositionPending;
        this.blocked = blocked;
        this.debt = debt;
    }

    public static BalanceDetails createFromJson(JsonElement element) {
        return buildGson().fromJson(element, BalanceDetails.class);
    }

    public BigDecimal getTotal() {
        return total;
    }

    public BigDecimal getAvailable() {
        return available;
    }

    public BigDecimal getDepositionPending() {
        return depositionPending;
    }

    public BigDecimal getBlocked() {
        return blocked;
    }

    public BigDecimal getDebt() {
        return debt;
    }

    private static Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(BalanceDetails.class, new Deserializer())
                .create();
    }

    private static final class Deserializer implements JsonDeserializer<BalanceDetails> {
        @Override
        public BalanceDetails deserialize(JsonElement json, Type typeOfT,
                                          JsonDeserializationContext context)
                throws JsonParseException {

            JsonObject object = json.getAsJsonObject();
            return new BalanceDetails(JsonUtils.getMandatoryBigDecimal(object, "total"),
                    JsonUtils.getMandatoryBigDecimal(object, "available"),
                    JsonUtils.getBigDecimal(object, "deposition_pending"),
                    JsonUtils.getBigDecimal(object, "blocked"),
                    JsonUtils.getBigDecimal(object, "debt"));
        }
    }
}
