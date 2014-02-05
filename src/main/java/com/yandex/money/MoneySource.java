package com.yandex.money;

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

    public MoneySource(WalletSource wallet) {
        this.wallet = wallet;
    }

    public static class WalletSource {

        private boolean allowed;

        public WalletSource(boolean allowed) {
            this.allowed = allowed;
        }

        public boolean isAllowed() {
            return allowed;
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
    }
}
