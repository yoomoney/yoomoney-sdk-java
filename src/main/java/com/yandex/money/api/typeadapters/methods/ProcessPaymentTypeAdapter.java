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
import com.yandex.money.api.methods.ProcessPayment;
import com.yandex.money.api.model.DigitalGoods;
import com.yandex.money.api.typeadapters.BaseTypeAdapter;
import com.yandex.money.api.typeadapters.model.DigitalGoodsTypeAdapter;

import java.lang.reflect.Type;

import static com.yandex.money.api.typeadapters.JsonUtils.getBigDecimal;
import static com.yandex.money.api.typeadapters.JsonUtils.getString;

/**
 * Type adapter for {@link ProcessPayment}.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class ProcessPaymentTypeAdapter extends BaseTypeAdapter<ProcessPayment> {

    private static final ProcessPaymentTypeAdapter INSTANCE = new ProcessPaymentTypeAdapter();

    private static final String MEMBER_ACCOUNT_UNBLOCK_URI = "account_unblock_uri";
    private static final String MEMBER_BALANCE = "balance";
    private static final String MEMBER_CREDIT_AMOUNT = "credit_amount";
    private static final String MEMBER_DIGITAL_GOODS = "digital_goods";
    private static final String MEMBER_HOLD_FOR_PICKUP_LINK = "hold_for_pickup_link";
    private static final String MEMBER_PAYEE = "payee";
    private static final String MEMBER_PAYEE_UID = "payee_uid";
    private static final String MEMBER_PAYER = "payer";
    private static final String MEMBER_PAYMENT_ID = "payment_id";

    private ProcessPaymentTypeAdapter() {
    }

    /**
     * @return instance of this class
     */
    public static ProcessPaymentTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    public ProcessPayment deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject object = json.getAsJsonObject();
        ProcessPayment.Builder builder = new ProcessPayment.Builder()
                .setPaymentId(getString(object, MEMBER_PAYMENT_ID))
                .setBalance(getBigDecimal(object, MEMBER_BALANCE))
                .setPayer(getString(object, MEMBER_PAYER))
                .setPayee(getString(object, MEMBER_PAYEE))
                .setCreditAmount(getBigDecimal(object, MEMBER_CREDIT_AMOUNT))
                .setAccountUnblockUri(getString(object, MEMBER_ACCOUNT_UNBLOCK_URI))
                .setPayeeUid(getString(object, MEMBER_PAYEE_UID))
                .setHoldForPickupLink(getString(object, MEMBER_HOLD_FOR_PICKUP_LINK))
                .setDigitalGoods(deserializeDigitalGoods(object));
        BaseProcessPaymentTypeAdapter.Delegate.deserialize(object, builder);
        return builder.create();
    }

    @Override
    public JsonElement serialize(ProcessPayment src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(MEMBER_PAYMENT_ID, src.paymentId);
        jsonObject.addProperty(MEMBER_BALANCE, src.balance);
        jsonObject.addProperty(MEMBER_PAYER, src.payer);
        jsonObject.addProperty(MEMBER_PAYEE, src.payee);
        jsonObject.addProperty(MEMBER_CREDIT_AMOUNT, src.creditAmount);
        jsonObject.addProperty(MEMBER_ACCOUNT_UNBLOCK_URI, src.accountUnblockUri);
        jsonObject.addProperty(MEMBER_PAYEE_UID, src.payeeUid);
        jsonObject.addProperty(MEMBER_HOLD_FOR_PICKUP_LINK, src.holdForPickupLink);
        if (src.digitalGoods != null) {
            jsonObject.add(MEMBER_DIGITAL_GOODS, DigitalGoodsTypeAdapter.getInstance().toJsonTree(
                    src.digitalGoods));
        }
        BaseProcessPaymentTypeAdapter.Delegate.serialize(jsonObject, src);
        return jsonObject;
    }

    @Override
    protected Class<ProcessPayment> getType() {
        return ProcessPayment.class;
    }

    private static DigitalGoods deserializeDigitalGoods(JsonObject jsonObject) {
        JsonElement digitalGoods = jsonObject.get(MEMBER_DIGITAL_GOODS);
        if (digitalGoods != null) {
            JsonObject jsonDigitalGoods = digitalGoods.getAsJsonObject();
            return DigitalGoodsTypeAdapter.getInstance().fromJson(jsonDigitalGoods);
        } else {
            return null;
        }
    }
}
