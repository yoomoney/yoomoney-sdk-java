package com.yandex.money.model.cps.misc;

import com.google.gson.JsonObject;
import com.yandex.money.Utils;

/**
 *
 */
public class MoneySource {

    private static final String FIELD_TYPE = "type";
    private static final String FIELD_PAYMENT_CARD_TYPE = "payment_card_type";
    private static final String FIELD_PAN_FRAGMENT = "pan_fragment";
    private static final String FIELD_MONEY_SOURCE_TOKEN = "money_source_token";

    private String type;
    private String paymentCardType;
    private String panFragment;
    private String moneySourceToken;

    public MoneySource(String type, String paymentCardType, String panFragment, String moneySourceToken) {
        this.type = type;
        this.paymentCardType = paymentCardType;
        this.panFragment = panFragment;
        this.moneySourceToken = moneySourceToken;
    }

    public static MoneySource parseJson(JsonObject obj) {
        if (obj != null) {
            String type = Utils.getString(obj, FIELD_TYPE);
            String paymentCardType = Utils.getString(obj, FIELD_PAYMENT_CARD_TYPE);
            String moneySourceToken = Utils.getString(obj, FIELD_MONEY_SOURCE_TOKEN);
            String panFragment = Utils.getString(obj, FIELD_PAN_FRAGMENT);

            return new MoneySource(type, paymentCardType, panFragment, moneySourceToken);
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
