package com.yandex.money.model.common;

import com.google.gson.JsonObject;
import com.yandex.money.Utils;

/**
 *
 */
public class MoneySource {

    private WalletSource wallet;
    private CardSource card;

    public MoneySource(WalletSource wallet, CardSource card) {
        this.wallet = wallet;
        this.card = card;
    }

    public static MoneySource parseJson(JsonObject obj) {
        if (obj != null) {
            JsonObject walletObj = obj.getAsJsonObject("wallet");
            WalletSource wallet = WalletSource.parseJson(walletObj);

            JsonObject cardObj = obj.getAsJsonObject("card");
            CardSource card = CardSource.parseJson(cardObj);

            return new MoneySource(wallet, card);
        }

        return null;
    }

    public static class WalletSource {

        private boolean allowed;

        public WalletSource(boolean allowed) {
            this.allowed = allowed;
        }

        public boolean isAllowed() {
            return allowed;
        }

        public static WalletSource parseJson(JsonObject obj) {
            if (obj != null) {
                Boolean allowed = Utils.getBoolean(obj, "allowed");
                if (allowed != null) {
                    return new WalletSource(allowed);
                }
            }
            return null;
        }
    }

    public static class CardSource extends WalletSource {

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
                Boolean allowed = Utils.getBoolean(obj, "allowed");
                Boolean cscRequired = Utils.getBoolean(obj, "csc_required");
                String panFragment = Utils.getString(obj, "pan_fragment");
                String type = Utils.getString(obj, "type");

                return new CardSource(allowed, cscRequired, panFragment, type);
            }
            return null;
        }
    }
}
