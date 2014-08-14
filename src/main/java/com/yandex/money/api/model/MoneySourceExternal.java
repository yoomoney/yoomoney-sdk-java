package com.yandex.money.api.model;

import com.google.gson.JsonObject;
import com.yandex.money.api.methods.JsonUtils;

/**
 * Money Source for external payments.
 *
 * @author Dmitriy Melnikov (dvmelnikov@yamoney.ru)
 * @see com.yandex.money.api.methods.RequestExternalPayment
 * @see com.yandex.money.api.methods.ProcessExternalPayment
 */
public class MoneySourceExternal {

    private static final String FIELD_TYPE = "type";
    private static final String FIELD_PAYMENT_CARD_TYPE = "payment_card_type";
    private static final String FIELD_PAN_FRAGMENT = "pan_fragment";
    private static final String FIELD_MONEY_SOURCE_TOKEN = "money_source_token";

    private final String type;
    private final String paymentCardType;
    private final String panFragment;
    private final String moneySourceToken;

    /**
     * Constructor.
     *
     * @param type type of money source
     * @param paymentCardType payment card type (e.g. VISA, MasterCard, AmericanExpress, etc.)
     * @param panFragment panned fragment of card's number
     * @param moneySourceToken token for reuse of money source in other payments
     */
    public MoneySourceExternal(String type, String paymentCardType, String panFragment,
                               String moneySourceToken) {

        this.type = type;
        this.paymentCardType = paymentCardType;
        this.panFragment = panFragment;
        this.moneySourceToken = moneySourceToken;
    }

    /**
     * Creates {@link com.yandex.money.api.model.MoneySourceExternal} from JSON.
     */
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

    /**
     * @return type of money source
     */
    public String getType() {
        return type;
    }

    /**
     * @return payment card type (e.g. VISA, MasterCard, AmericanExpress, etc.)
     */
    public String getPaymentCardType() {
        return paymentCardType;
    }

    /**
     * @return panned fragment of card's number
     */
    public String getPanFragment() {
        return panFragment;
    }

    /**
     * @return token for reuse of money source in other payments
     */
    public String getMoneySourceToken() {
        return moneySourceToken;
    }
}
