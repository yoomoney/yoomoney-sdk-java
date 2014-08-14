package com.yandex.money.api.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.api.methods.JsonUtils;

import java.lang.reflect.Type;
import java.math.BigDecimal;

/**
 * Detailed balance info.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class BalanceDetails {

    private final BigDecimal total;
    private final BigDecimal available;
    private final BigDecimal depositionPending;
    private final BigDecimal blocked;
    private final BigDecimal debt;

    /**
     * Constructor
     *
     * @param total total balance
     * @param available available balance
     * @param depositionPending  pending deposition
     * @param blocked money blocked
     * @param debt account's debt
     */
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

    /**
     * Creates {@link com.yandex.money.api.model.BalanceDetails} from JSON.
     *
     * @param element JSON object
     * @return {@link com.yandex.money.api.model.BalanceDetails}
     */
    public static BalanceDetails createFromJson(JsonElement element) {
        return buildGson().fromJson(element, BalanceDetails.class);
    }

    /**
     * @return total balance
     */
    public BigDecimal getTotal() {
        return total;
    }

    /**
     * @return available balance
     */
    public BigDecimal getAvailable() {
        return available;
    }

    /**
     * @return pending deposition
     */
    public BigDecimal getDepositionPending() {
        return depositionPending;
    }

    /**
     * @return money blocked
     */
    public BigDecimal getBlocked() {
        return blocked;
    }

    /**
     * @return account's debt
     */
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
