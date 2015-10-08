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
import com.yandex.money.api.typeadapters.BaseRequestPaymentTypeAdapter.Delegate;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.yandex.money.api.methods.JsonUtils.getBigDecimal;
import static com.yandex.money.api.methods.JsonUtils.getMandatoryBoolean;
import static com.yandex.money.api.methods.JsonUtils.getNotNullArray;
import static com.yandex.money.api.methods.JsonUtils.getString;

/**
 * Type adapter for {@link RequestPayment}.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public class RequestPaymentTypeAdapter extends BaseTypeAdapter<RequestPayment> {

    private static final RequestPaymentTypeAdapter INSTANCE = new RequestPaymentTypeAdapter();

    private static final String MEMBER_ACCOUNT_UNBLOCK_URI = "account_unblock_uri";
    private static final String MEMBER_BALANCE = "balance";
    private static final String MEMBER_EXT_ACTION_URI = "ext_action_uri";
    private static final String MEMBER_MS = "money_sources";
    private static final String MEMBER_MS_ALLOWED = "allowed";
    private static final String MEMBER_MS_CARDS = "cards";
    private static final String MEMBER_MS_CSC_REQUIRED = "csc_required";
    private static final String MEMBER_MS_ITEMS = "items";
    private static final String MEMBER_MS_WALLET = "wallet";
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
    public RequestPayment deserialize(JsonElement json, Type typeOfT,
                                      JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject object = json.getAsJsonObject();

        RequestPayment.Builder builder = new RequestPayment.Builder()
                .setBalance(getBigDecimal(object, MEMBER_BALANCE))
                .setRecipientAccountStatus(AccountStatus.parse(
                        getString(object, MEMBER_RECIPIENT_ACCOUNT_STATUS)))
                .setRecipientAccountType(AccountType.parse(
                        getString(object, MEMBER_RECIPIENT_ACCOUNT_TYPE)))
                .setProtectionCode(getString(object, MEMBER_PROTECTION_CODE))
                .setAccountUnblockUri(getString(object, MEMBER_ACCOUNT_UNBLOCK_URI))
                .setExtActionUri(getString(object, MEMBER_EXT_ACTION_URI));

        JsonObject moneySources = object.getAsJsonObject(MEMBER_MS);
        if (moneySources != null) {
            deserializeMoneySources(moneySources, builder);
        }
        Delegate.deserialize(object, builder);

        return builder.create();
    }

    @Override
    public JsonElement serialize(RequestPayment src, Type typeOfSrc,
                                 JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty(MEMBER_BALANCE, src.balance);
        jsonObject.addProperty(MEMBER_RECIPIENT_ACCOUNT_STATUS, src.recipientAccountStatus.code);
        jsonObject.addProperty(MEMBER_RECIPIENT_ACCOUNT_TYPE, src.recipientAccountType.code);
        jsonObject.addProperty(MEMBER_PROTECTION_CODE, src.protectionCode);
        jsonObject.addProperty(MEMBER_ACCOUNT_UNBLOCK_URI, src.accountUnblockUri);
        jsonObject.addProperty(MEMBER_EXT_ACTION_URI, src.extActionUri);

        if (!src.moneySources.isEmpty()) {
            jsonObject.add(MEMBER_MS, serializeMoneySources(src.moneySources,
                    src.cscRequired));
        }
        Delegate.serialize(jsonObject, src);
        return jsonObject;
    }

    @Override
    protected Class<RequestPayment> getType() {
        return RequestPayment.class;
    }

    private static JsonElement serializeMoneySources(List<MoneySource> moneySources,
                                                     boolean cscRequired) {
        JsonArray cards = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        for (MoneySource moneySource : moneySources) {
            if (moneySource instanceof Card) {
                cards.add(CardTypeAdapter.getInstance().toJsonTree((Card) moneySource));
            } else if (moneySource instanceof Wallet) {
                JsonObject wallet = new JsonObject();
                wallet.addProperty(MEMBER_MS_ALLOWED, true);
                jsonObject.add(MEMBER_MS_WALLET, wallet);
            }
        }
        if (cards.size() == 0) {
            return jsonObject;
        } else {
            JsonObject jsonCards = new JsonObject();
            jsonCards.addProperty(MEMBER_MS_CSC_REQUIRED, cscRequired);
            jsonCards.addProperty(MEMBER_MS_ALLOWED, true);
            jsonCards.add(MEMBER_MS_ITEMS, cards);
            jsonObject.add(MEMBER_MS_CARDS, jsonCards);
            return jsonObject;
        }
    }

    private static void deserializeMoneySources(JsonObject object, RequestPayment.Builder builder) {
        List<MoneySource> moneySources = new ArrayList<>();

        JsonObject wallet = object.getAsJsonObject(MEMBER_MS_WALLET);
        if (wallet != null && getMandatoryBoolean(wallet, MEMBER_MS_ALLOWED)) {
            moneySources.add(Wallet.INSTANCE);
        }

        JsonObject cards = object.getAsJsonObject(MEMBER_MS_CARDS);
        if (cards != null) {
            builder.setCscRequired(getMandatoryBoolean(cards, MEMBER_MS_CSC_REQUIRED));
            if (getMandatoryBoolean(cards, MEMBER_MS_ALLOWED)) {
                moneySources.addAll(getNotNullArray(cards, MEMBER_MS_ITEMS,
                        CardTypeAdapter.getInstance()));
            }
        }
        builder.setMoneySources(moneySources);
    }
}
