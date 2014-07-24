package com.yandex.money.model.methods;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.model.methods.misc.AccountStatus;
import com.yandex.money.model.methods.misc.AccountType;
import com.yandex.money.model.methods.misc.Avatar;
import com.yandex.money.model.methods.misc.BalanceDetails;
import com.yandex.money.model.methods.misc.Card;
import com.yandex.money.net.HostsProvider;
import com.yandex.money.net.MethodRequest;
import com.yandex.money.net.MethodResponse;
import com.yandex.money.net.PostRequestBodyBuffer;
import com.yandex.money.utils.Currency;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;

public class AccountInfo implements MethodResponse {

    private final String account;
    private final BigDecimal balance;
    private final Currency currency;
    private final AccountStatus accountStatus;
    private final AccountType accountType;
    private final Avatar avatar;
    private final BalanceDetails balanceDetails;
    private final Card[] linkedCards;
    private final String[] additionalServices;

    public AccountInfo(String account, BigDecimal balance, Currency currency,
                       AccountStatus accountStatus, AccountType accountType, Avatar avatar,
                       BalanceDetails balanceDetails, Card[] linkedCards,
                       String[] additionalServices) {

        if (account == null) {
            throw new JsonParseException("Has no mandatory field 'account'");
        }
        this.account = account;
        if (balance == null) {
            throw new JsonParseException("Has no mandatory field 'balance'");
        }
        this.balance = balance;
        this.currency = currency;
        this.accountStatus = accountStatus;
        this.accountType = accountType;
        this.avatar = avatar;
        this.balanceDetails = balanceDetails;
        this.linkedCards = linkedCards;
        this.additionalServices = additionalServices;
    }

    public String getAccount() {
        return account;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public Currency getCurrency() {
        return currency;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public BalanceDetails getBalanceDetails() {
        return balanceDetails;
    }

    public Card[] getLinkedCards() {
        return linkedCards;
    }

    public String[] getAdditionalServices() {
        return additionalServices;
    }

    public static final class Request implements MethodRequest<AccountInfo> {

        @Override
        public URL requestURL(HostsProvider hostsProvider) throws MalformedURLException {
            return new URL(hostsProvider.getMoneyApi() + "/account-info");
        }

        @Override
        public AccountInfo parseResponse(InputStream inputStream) {
            return buildGson().fromJson(new InputStreamReader(inputStream), AccountInfo.class);
        }

        @Override
        public PostRequestBodyBuffer buildParameters() throws IOException {
            return null;
        }

        private static Gson buildGson() {
            return new GsonBuilder()
                    .registerTypeAdapter(AccountInfo.class, new Deserializer())
                    .create();
        }
    }

    private static final class Deserializer implements JsonDeserializer<AccountInfo> {
        @Override
        public AccountInfo deserialize(JsonElement json, Type typeOfT,
                                       JsonDeserializationContext context)
                throws JsonParseException {

            JsonObject object = json.getAsJsonObject();

            Currency currency;
            try {
                int parsed = Integer.parseInt(JsonUtils.getString(object, "currency"));
                currency = Currency.parseNumericCode(parsed);
            } catch (NumberFormatException e) {
                currency = Currency.RUB;
            }

            final String memberAvatar = "avatar";
            Avatar avatar = null;
            if (object.has(memberAvatar)) {
                avatar = Avatar.createFromJson(object.get(memberAvatar));
            }

            final String memberBalanceDetails = "balance_details";
            BalanceDetails balanceDetails = null;
            if (object.has(memberBalanceDetails)) {
                balanceDetails = BalanceDetails.createFromJson(object.get(memberBalanceDetails));
            }

            final String memberLinkedCards = "cards_linked";
            Card[] linkedCards = null;
            if (object.has(memberLinkedCards)) {
                JsonArray array = object.getAsJsonArray(memberLinkedCards);
                final int size = array.size();
                linkedCards = new Card[size];
                for (int i = 0; i < size; ++i) {
                    linkedCards[i] = Card.createFromJson(array.get(i));
                }
            }

            final String memberAdditionalServices = "services_additional";
            String[] additionalServices = null;
            if (object.has(memberAdditionalServices)) {
                JsonArray array = object.getAsJsonArray(memberAdditionalServices);
                final int size = array.size();
                additionalServices = new String[size];
                for (int i = 0; i < size; ++i) {
                    additionalServices[i] = array.get(i).getAsString();
                }
            }

            return new AccountInfo(JsonUtils.getMandatoryString(object, "account"),
                    JsonUtils.getMandatoryBigDecimal(object, "balance"), currency,
                    AccountStatus.parse(JsonUtils.getString(object, "account_status")),
                    AccountType.parse(JsonUtils.getString(object, "account_type")),
                    avatar, balanceDetails, linkedCards, additionalServices);
        }
    }
}
