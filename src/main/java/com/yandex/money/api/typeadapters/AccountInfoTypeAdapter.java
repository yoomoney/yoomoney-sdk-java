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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.methods.AccountInfo;
import com.yandex.money.api.methods.JsonUtils;
import com.yandex.money.api.model.AccountStatus;
import com.yandex.money.api.model.AccountType;
import com.yandex.money.api.model.Avatar;
import com.yandex.money.api.model.BalanceDetails;
import com.yandex.money.api.model.Card;
import com.yandex.money.api.utils.Currency;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public final class AccountInfoTypeAdapter implements TypeAdapter<AccountInfo> {

    private static final AccountInfoTypeAdapter INSTANCE = new AccountInfoTypeAdapter();

    private static final String MEMBER_ACCOUNT = "account";
    private static final String MEMBER_AVATAR = "avatar";
    private static final String MEMBER_BALANCE = "balance";
    private static final String MEMBER_BALANCE_DETAILS = "balance_details";
    private static final String MEMBER_CARDS_LINKED = "cards_linked";
    private static final String MEMBER_CURRENCY = "currency";
    private static final String MEMBER_SERVICES_ADDITIONAL = "services_additional";
    private static final String MEMBER_STATUS = "account_status";
    private static final String MEMBER_TYPE = "account_type";

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(AccountInfo.class, INSTANCE)
            .create();

    private AccountInfoTypeAdapter() {
    }

    /**
     * @return instance of this class
     */
    public static AccountInfoTypeAdapter getInstance() {
        return INSTANCE;
    }

    /**
     * Creates {@link AccountInfo} from json.
     *
     * @param json json string
     * @return account info
     */
    public static AccountInfo fromJson(String json) {
        return GSON.fromJson(json, AccountInfo.class);
    }

    /**
     * Creates {@link AccountInfo} from json.
     *
     * @param element json element
     * @return account info
     */
    public static AccountInfo fromJson(JsonElement element) {
        return GSON.fromJson(element, AccountInfo.class);
    }

    /**
     * Serializes {@link AccountInfo} to json string.
     *
     * @param accountInfo account info
     * @return json string
     */
    public static String toJson(AccountInfo accountInfo) {
        return GSON.toJson(accountInfo);
    }

    /**
     * Serializes {@link AccountInfo} to json tree.
     *
     * @param accountInfo account info
     * @return json element
     */
    public static JsonElement toJsonTree(AccountInfo accountInfo) {
        return GSON.toJsonTree(accountInfo);
    }

    @Override
    public AccountInfo deserialize(JsonElement json, Type typeOfT,
                                   JsonDeserializationContext context) throws JsonParseException {

        JsonObject object = json.getAsJsonObject();

        Currency currency;
        try {
            int parsed = Integer.parseInt(JsonUtils.getString(object, MEMBER_CURRENCY));
            currency = Currency.parseNumericCode(parsed);
        } catch (NumberFormatException e) {
            currency = Currency.RUB;
        }

        Avatar avatar = null;
        if (object.has(MEMBER_AVATAR)) {
            avatar = AvatarTypeAdapter.fromJson(object.get(MEMBER_AVATAR));
        }

        BalanceDetails balanceDetails = BalanceDetailsTypeAdapter.fromJson(
                object.get(MEMBER_BALANCE_DETAILS));

        List<Card> linkedCards = new ArrayList<>();
        if (object.has(MEMBER_CARDS_LINKED)) {
            JsonArray array = object.getAsJsonArray(MEMBER_CARDS_LINKED);
            final int size = array.size();
            for (int i = 0; i < size; ++i) {
                linkedCards.add(CardTypeAdapter.fromJson(array.get(i)));
            }
        }

        List<String> additionalServices = new ArrayList<>();
        if (object.has(MEMBER_SERVICES_ADDITIONAL)) {
            JsonArray array = object.getAsJsonArray(MEMBER_SERVICES_ADDITIONAL);
            final int size = array.size();
            for (int i = 0; i < size; ++i) {
                additionalServices.add(array.get(i).getAsString());
            }
        }

        return new AccountInfo(JsonUtils.getMandatoryString(object, MEMBER_ACCOUNT),
                JsonUtils.getMandatoryBigDecimal(object, MEMBER_BALANCE), currency,
                AccountStatus.parse(JsonUtils.getMandatoryString(object, MEMBER_STATUS)),
                AccountType.parse(JsonUtils.getMandatoryString(object, MEMBER_TYPE)),
                avatar, balanceDetails, linkedCards, additionalServices);
    }

    @Override
    public JsonElement serialize(AccountInfo src, Type typeOfSrc,
                                 JsonSerializationContext context) {

        JsonObject object = new JsonObject();

        object.addProperty(MEMBER_ACCOUNT, src.account);
        object.addProperty(MEMBER_BALANCE, src.balance);
        object.addProperty(MEMBER_CURRENCY, src.currency.numericCode);
        object.addProperty(MEMBER_STATUS, src.accountStatus.code);
        object.addProperty(MEMBER_TYPE, src.accountType.code);

        object.add(MEMBER_AVATAR, AvatarTypeAdapter.toJsonTree(src.avatar));
        object.add(MEMBER_BALANCE_DETAILS, BalanceDetailsTypeAdapter.toJsonTree(
                src.balanceDetails));

        if (!src.linkedCards.isEmpty()) {
            JsonArray array = new JsonArray();
            for (Card card : src.linkedCards) {
                array.add(CardTypeAdapter.toJsonTree(card));
            }
            object.add(MEMBER_CARDS_LINKED, array);
        }

        if (!src.additionalServices.isEmpty()) {
            JsonArray array = new JsonArray();
            for (String service : src.additionalServices) {
                array.add(new JsonPrimitive(service));
            }
            object.add(MEMBER_SERVICES_ADDITIONAL, array);
        }

        return object;
    }
}
