/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 NBCO Yandex.Money LLC
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

package com.yandex.money.api.typeadapters.methods;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.methods.AccountInfo;
import com.yandex.money.api.model.AccountStatus;
import com.yandex.money.api.model.AccountType;
import com.yandex.money.api.model.BalanceDetails;
import com.yandex.money.api.typeadapters.BaseTypeAdapter;
import com.yandex.money.api.typeadapters.model.BalanceDetailsTypeAdapter;
import com.yandex.money.api.util.Currency;

import java.lang.reflect.Type;
import java.math.BigDecimal;

import static com.yandex.money.api.typeadapters.JsonUtils.getBigDecimal;
import static com.yandex.money.api.typeadapters.JsonUtils.getString;

/**
 * Type adapter for {@link AccountInfo}.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public final class AccountInfoTypeAdapter extends BaseTypeAdapter<AccountInfo> {

    private static final AccountInfoTypeAdapter INSTANCE = new AccountInfoTypeAdapter();

    private AccountInfoTypeAdapter() {
    }

    /**
     * @return instance of this class
     */
    public static AccountInfoTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    public AccountInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        AccountInfo.Builder builder = new AccountInfo.Builder();
        Delegate.deserialize(json.getAsJsonObject(), builder);
        return builder.create();
    }

    @Override
    public JsonElement serialize(AccountInfo src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        Delegate.serialize(object, src);
        return object;
    }

    @Override
    protected Class<AccountInfo> getType() {
        return AccountInfo.class;
    }

    static class Delegate {

        private static final String MEMBER_ACCOUNT = "account";
        private static final String MEMBER_BALANCE = "balance";
        private static final String MEMBER_BALANCE_DETAILS = "balance_details";
        private static final String MEMBER_CURRENCY = "currency";
        private static final String MEMBER_STATUS = "account_status";
        private static final String MEMBER_TYPE = "account_type";

        private Delegate() {
        }

        static <T extends AccountInfo.Builder> void deserialize(JsonObject object, T builder) {
            Currency currency = null;
            try {
                String c = getString(object, MEMBER_CURRENCY);
                if (c != null) {
                    int parsed = Integer.parseInt(c);
                    currency = Currency.parseNumericCode(parsed);
                }
            } catch (NumberFormatException e) {
                // see code below
            }

            BigDecimal balance = getBigDecimal(object, MEMBER_BALANCE);
            BalanceDetails balanceDetails = BalanceDetailsTypeAdapter.getInstance()
                    .fromJson(object.get(MEMBER_BALANCE_DETAILS));

            builder.setAccount(getString(object, MEMBER_ACCOUNT))
                    .setBalance(balance)
                    .setCurrency(currency)
                    .setAccountStatus(AccountStatus.parse(getString(object, MEMBER_STATUS)))
                    .setAccountType(AccountType.parse(getString(object, MEMBER_TYPE)))
                    .setBalanceDetails(balanceDetails);
        }

        static <T extends AccountInfo> void serialize(JsonObject object, T src) {
            object.addProperty(MEMBER_ACCOUNT, src.account);
            object.addProperty(MEMBER_BALANCE, src.balance);
            object.addProperty(MEMBER_CURRENCY, src.currency.numericCode.toString());
            object.addProperty(MEMBER_STATUS, src.accountStatus.code);
            object.addProperty(MEMBER_TYPE, src.accountType.code);
            object.add(MEMBER_BALANCE_DETAILS, BalanceDetailsTypeAdapter.getInstance().toJsonTree(src.balanceDetails));
        }
    }
}
