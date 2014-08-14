package com.yandex.money.model;

import com.google.gson.JsonObject;
import com.yandex.money.methods.JsonUtils;

/**
 *
 */
public class MoneySourceExternal {

    private static final String FIELD_TYPE = "type";
    private static final String FIELD_PAYMENT_CARD_TYPE = "payment_card_type";
    private static final String FIELD_PAN_FRAGMENT = "pan_fragment";
    private static final String FIELD_MONEY_SOURCE_TOKEN = "money_source_token";

    private String type;
    private String paymentCardType;
    private String panFragment;
    private String moneySourceToken;

    public MoneySourceExternal(String type, String paymentCardType, String panFragment, String moneySourceToken) {
        this.type = type;
        this.paymentCardType = paymentCardType;
        this.panFragment = panFragment;
        this.moneySourceToken = moneySourceToken;
    }

    public static MoneySourceExternal parseJson(JsonObject obj) {
        if (obj != null) {
            String type = JsonUtils.getString(obj, FIELD_TYPE);
            String paymentCardType = JsonUtils.getString(obj, FIELD_PAYMENT_CARD_TYPE);
            String moneySourceToken = JsonUtils.getString(obj, FIELD_MONEY_SOURCE_TOKEN);
            String panFragment = JsonUtils.getString(obj, FIELD_PAN_FRAGMENT);

            return new MoneySourceExternal(type, paymentCardType, panFragment, moneySourceToken);
        }
        return null;
    }

    public String getType() {
        return type;
    }

    public String getPaymentCardType() {
        return paymentCardType;
    }

    public String getPanFragment() {
        return panFragment;
    }

    public String getMoneySourceToken() {
        return moneySourceToken;
    }
}
