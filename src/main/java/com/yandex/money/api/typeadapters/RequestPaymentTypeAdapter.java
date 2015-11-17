/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 NBCO Yandex.Money LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.yandex.money.api.typeadapters;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.methods.RequestPayment;
import com.yandex.money.api.model.AccountStatus;
import com.yandex.money.api.model.AccountType;
import com.yandex.money.api.model.Card;
import com.yandex.money.api.model.MoneySource;
import com.yandex.money.api.model.Wallet;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.yandex.money.api.typeadapters.JsonUtils.getBigDecimal;
import static com.yandex.money.api.typeadapters.JsonUtils.getMandatoryBoolean;
import static com.yandex.money.api.typeadapters.JsonUtils.getNotNullArray;
import static com.yandex.money.api.typeadapters.JsonUtils.getString;

/**
 * Type adapter for {@link RequestPayment}.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class RequestPaymentTypeAdapter extends BaseTypeAdapter<RequestPayment> {

    private static final RequestPaymentTypeAdapter INSTANCE = new RequestPaymentTypeAdapter();

    private static final String MEMBER_ACCOUNT_UNBLOCK_URI = "account_unblock_uri";
    private static final String MEMBER_BALANCE = "balance";
    private static final String MEMBER_EXT_ACTION_URI = "ext_action_uri";
    private static final String MEMBER_MONEY_SOURCE = "money_source";
    private static final String MEMBER_PROTECTION_CODE = "protection_code";
    private static final String MEMBER_RECIPIENT_ACCOUNT_STATUS = "recipient_account_status";
    private static final String MEMBER_RECIPIENT_ACCOUNT_TYPE = "recipient_account_type";

    private RequestPaymentTypeAdapter() {
    }

    /**
     * @return instance of this class
     */
    public static RequestPaymentTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    public RequestPayment deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject object = json.getAsJsonObject();

        RequestPayment.Builder builder = new RequestPayment.Builder()
                .setBalance(getBigDecimal(object, MEMBER_BALANCE))
                .setRecipientAccountStatus(AccountStatus.parse(getString(object, MEMBER_RECIPIENT_ACCOUNT_STATUS)))
                .setRecipientAccountType(AccountType.parse(getString(object, MEMBER_RECIPIENT_ACCOUNT_TYPE)))
                .setProtectionCode(getString(object, MEMBER_PROTECTION_CODE))
                .setAccountUnblockUri(getString(object, MEMBER_ACCOUNT_UNBLOCK_URI))
                .setExtActionUri(getString(object, MEMBER_EXT_ACTION_URI));

        JsonObject moneySourceObject = object.getAsJsonObject(MEMBER_MONEY_SOURCE);
        if (moneySourceObject != null) {
            builder.setMoneySources(MoneySourceListTypeAdapter.Delegate.deserialize(moneySourceObject, builder));
        }

        BaseRequestPaymentTypeAdapter.Delegate.deserialize(object, builder);
        return builder.create();
    }

    @Override
    public JsonElement serialize(RequestPayment src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty(MEMBER_BALANCE, src.balance);
        jsonObject.addProperty(MEMBER_PROTECTION_CODE, src.protectionCode);
        jsonObject.addProperty(MEMBER_ACCOUNT_UNBLOCK_URI, src.accountUnblockUri);
        jsonObject.addProperty(MEMBER_EXT_ACTION_URI, src.extActionUri);

        if (src.recipientAccountStatus != null) {
            jsonObject.addProperty(MEMBER_RECIPIENT_ACCOUNT_STATUS, src.recipientAccountStatus.code);
        }
        if (src.recipientAccountType != null) {
            jsonObject.addProperty(MEMBER_RECIPIENT_ACCOUNT_TYPE, src.recipientAccountType.code);
        }
        if (!src.moneySources.isEmpty()) {
            jsonObject.add(MEMBER_MONEY_SOURCE, MoneySourceListTypeAdapter.Delegate.serialize(
                    src.moneySources, src.cscRequired));
        }

        BaseRequestPaymentTypeAdapter.Delegate.serialize(jsonObject, src);
        return jsonObject;
    }

    @Override
    protected Class<RequestPayment> getType() {
        return RequestPayment.class;
    }

    private static final class MoneySourceListTypeAdapter {

        private static final String MEMBER_ALLOWED = "allowed";
        private static final String MEMBER_CARDS = "cards";
        private static final String MEMBER_CSC_REQUIRED = "csc_required";
        private static final String MEMBER_ITEMS = "items";
        private static final String MEMBER_WALLET = "wallet";

        private MoneySourceListTypeAdapter() {
        }

        static final class Delegate {

            private Delegate() {
            }

            static List<MoneySource> deserialize(JsonObject object, RequestPayment.Builder builder) {
                List<MoneySource> list = new ArrayList<>();

                JsonObject walletObject = object.getAsJsonObject(MEMBER_WALLET);
                if (walletObject != null && getMandatoryBoolean(walletObject, MEMBER_ALLOWED)) {
                    list.add(Wallet.INSTANCE);
                }

                JsonObject cardsObject = object.getAsJsonObject(MEMBER_CARDS);
                if (cardsObject != null && getMandatoryBoolean(cardsObject, MEMBER_ALLOWED)) {
                    builder.setCscRequired(getMandatoryBoolean(cardsObject, MEMBER_CSC_REQUIRED));
                    list.addAll(getNotNullArray(cardsObject, MEMBER_ITEMS, CardTypeAdapter.getInstance()));
                }

                return list;
            }

            static JsonElement serialize(List<MoneySource> src, boolean cscRequired) {
                JsonObject object = new JsonObject();
                JsonArray itemsArray = new JsonArray();
                for (MoneySource moneySource : src) {
                    Class<? extends MoneySource> cls = moneySource.getClass();
                    if (cls == Card.class) {
                        itemsArray.add(CardTypeAdapter.getInstance().toJsonTree((Card) moneySource));
                    } else if (cls == Wallet.class) {
                        JsonObject walletObject = new JsonObject();
                        walletObject.addProperty(MEMBER_ALLOWED, true);
                        object.add(MEMBER_WALLET, walletObject);
                    }
                }

                if (itemsArray.size() > 0) {
                    JsonObject cardsObject = new JsonObject();
                    cardsObject.addProperty(MEMBER_ALLOWED, true);
                    cardsObject.addProperty(MEMBER_CSC_REQUIRED, cscRequired);
                    cardsObject.add(MEMBER_ITEMS, itemsArray);
                    object.add(MEMBER_CARDS, cardsObject);
                }

                return object;
            }
        }
    }
}
