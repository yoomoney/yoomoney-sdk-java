package com.yandex.money.model.common;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.Utils;

import java.lang.reflect.Type;

/**
 *
 */
public class MoneySource {

    private static final String FIELD_WALLET = "wallet";
    private static final String FIELD_CARD = "card";

    private WalletSource wallet;
    private CardSource card;

    public MoneySource(WalletSource wallet, CardSource card) {
        this.wallet = wallet;
        this.card = card;
    }

    public WalletSource getWallet() {
        return wallet;
    }

    public CardSource getCard() {
        return card;
    }

    public static MoneySource parseJson(String json) {
        return new GsonBuilder().registerTypeAdapter(MoneySource.class,
                new JsonDeserializer<MoneySource>() {
                    @Override
                    public MoneySource deserialize(JsonElement json, Type typeOfT,
                                                   JsonDeserializationContext context)
                            throws JsonParseException {

                        return parseJson(json.getAsJsonObject());
                    }
                }
        ).create().fromJson(json, MoneySource.class);
    }

    public static MoneySource parseJson(JsonObject obj) {
        if (obj != null) {
            JsonObject walletObj = obj.getAsJsonObject(FIELD_WALLET);
            WalletSource wallet = WalletSource.parseJson(walletObj);

            JsonObject cardObj = obj.getAsJsonObject(FIELD_CARD);
            CardSource card = CardSource.parseJson(cardObj);

            return new MoneySource(wallet, card);
        }

        return null;
    }

    public static JsonObject toJson(MoneySource moneySource) {
        if (moneySource == null) {
            return null;
        }
        JsonObject object = new JsonObject();
        object.add(FIELD_WALLET, WalletSource.toJson(moneySource.getWallet()));
        object.add(FIELD_CARD, CardSource.toJson(moneySource.getCard()));
        return object;
    }

    public static class WalletSource {

        protected static final String FIELD_ALLOWED = "allowed";

        private boolean allowed;

        public WalletSource(boolean allowed) {
            this.allowed = allowed;
        }

        public boolean isAllowed() {
            return allowed;
        }

        public static WalletSource parseJson(JsonObject obj) {
            if (obj != null) {
                Boolean allowed = Utils.getBoolean(obj, FIELD_ALLOWED);
                if (allowed != null) {
                    return new WalletSource(allowed);
                }
            }
            return null;
        }

        public static JsonObject toJson(WalletSource walletSource) {
            if (walletSource == null) {
                return null;
            }
            JsonObject object = new JsonObject();
            object.addProperty(FIELD_ALLOWED, walletSource.isAllowed());
            return object;
        }
    }

    public static class CardSource extends WalletSource {

        private static final String FIELD_CSC_REQUIRED = "csc_required";
        private static final String FIELD_PAN_FRAGMENT = "pan_fragment";
        private static final String FIELD_TYPE = "type";

        private boolean cscRequired;
        private String panFragment;
        private String type;

        public CardSource(boolean allowed, boolean cscRequired, String panFragment, String type) {
            super(allowed);
            this.cscRequired = cscRequired;
            this.panFragment = panFragment;
            this.type = type;
        }

        public static CardSource parseJson(JsonObject obj) {
            if (obj != null) {
                Boolean allowed = Utils.getBoolean(obj, FIELD_ALLOWED);
                Boolean cscRequired = Utils.getBoolean(obj, FIELD_CSC_REQUIRED);
                String panFragment = Utils.getString(obj, FIELD_PAN_FRAGMENT);
                String type = Utils.getString(obj, FIELD_TYPE);

                return new CardSource(allowed, cscRequired, panFragment, type);
            }
            return null;
        }

        public static JsonObject toJson(CardSource cardSource) {
            if (cardSource == null) {
                return null;
            }
            JsonObject object = new JsonObject();
            object.addProperty(FIELD_ALLOWED, cardSource.isAllowed());
            object.addProperty(FIELD_CSC_REQUIRED, cardSource.isCscRequired());
            object.addProperty(FIELD_PAN_FRAGMENT, cardSource.getPanFragment());
            object.addProperty(FIELD_TYPE, cardSource.getType());
            return object;
        }

        public boolean isCscRequired() {
            return cscRequired;
        }

        public String getPanFragment() {
            return panFragment;
        }

        public String getType() {
            return type;
        }
    }
}
