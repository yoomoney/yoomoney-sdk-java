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

package com.yandex.money.api.typeadapters.model;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.reflect.TypeToken;
import com.yandex.money.api.model.Operation;
import com.yandex.money.api.model.PayeeIdentifierType;
import com.yandex.money.api.model.showcase.ShowcaseReference;
import com.yandex.money.api.time.Iso8601Format;
import com.yandex.money.api.typeadapters.BaseTypeAdapter;
import com.yandex.money.api.typeadapters.GsonProvider;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;

import static com.yandex.money.api.typeadapters.JsonUtils.getBigDecimal;
import static com.yandex.money.api.typeadapters.JsonUtils.getBoolean;
import static com.yandex.money.api.typeadapters.JsonUtils.getDateTime;
import static com.yandex.money.api.typeadapters.JsonUtils.getNotNullMap;
import static com.yandex.money.api.typeadapters.JsonUtils.getString;
import static com.yandex.money.api.typeadapters.JsonUtils.toJsonObject;

/**
 * Type adapter for {@link Operation}.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class OperationTypeAdapter extends BaseTypeAdapter<Operation> {

    private static final OperationTypeAdapter INSTANCE = new OperationTypeAdapter();

    private static final String MEMBER_AMOUNT = "amount";
    private static final String MEMBER_AMOUNT_DUE = "amount_due";
    private static final String MEMBER_ANSWER_DATETIME = "answer_datetime";
    private static final String MEMBER_CODEPRO = "codepro";
    private static final String MEMBER_COMMENT = "comment";
    private static final String MEMBER_DATETIME = "datetime";
    private static final String MEMBER_DETAILS = "details";
    private static final String MEMBER_DIGITAL_GOODS = "digital_goods";
    private static final String MEMBER_DIRECTION = "direction";
    private static final String MEMBER_EXPIRES = "expires";
    private static final String MEMBER_FAVOURITE = "favourite";
    private static final String MEMBER_FEE = "fee";
    private static final String MEMBER_LABEL = "label";
    private static final String MEMBER_MESSAGE = "message";
    private static final String MEMBER_OPERATION_ID = "operation_id";
    private static final String MEMBER_PATTERN_ID = "pattern_id";
    private static final String MEMBER_PAYMENT_PARAMETERS = "payment_parameters";
    private static final String MEMBER_PROTECTION_CODE = "protection_code";
    private static final String MEMBER_RECIPIENT = "recipient";
    private static final String MEMBER_RECIPIENT_TYPE = "recipient_type";
    private static final String MEMBER_REPEATABLE = "repeatable";
    private static final String MEMBER_SENDER = "sender";
    private static final String MEMBER_STATUS = "status";
    private static final String MEMBER_TITLE = "title";
    private static final String MEMBER_TYPE = "type";
    private static final String MEMBER_CATEGORIES = "categories";
    private static final String MEMBER_FORMAT = "format";

    private OperationTypeAdapter() {
    }

    /**
     * @return instance of this class
     */
    public static OperationTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    public Operation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        final JsonObject o = json.getAsJsonObject();
        Type listType = new TypeToken<ArrayList<Integer>>(){}.getType();
        try {
            return new Operation.Builder()
                    .setOperationId(getString(o, MEMBER_OPERATION_ID))
                    .setStatus(Operation.Status.parseOrThrow(getString(o, MEMBER_STATUS)))
                    .setDatetime(getDateTime(o, MEMBER_DATETIME))
                    .setTitle(getString(o, MEMBER_TITLE))
                    .setPatternId(getString(o, MEMBER_PATTERN_ID))
                    .setDirection(Operation.Direction.parseOrThrow(getString(o, MEMBER_DIRECTION)))
                    .setAmount(getBigDecimal(o, MEMBER_AMOUNT))
                    .setAmountDue(getBigDecimal(o, MEMBER_AMOUNT_DUE))
                    .setFee(getBigDecimal(o, MEMBER_FEE))
                    .setLabel(getString(o, MEMBER_LABEL))
                    .setType(Operation.Type.parseOrThrow(getString(o, MEMBER_TYPE)))
                    .setSender(getString(o, MEMBER_SENDER))
                    .setRecipient(getString(o, MEMBER_RECIPIENT))
                    .setRecipientType(PayeeIdentifierType.parse(getString(o, MEMBER_RECIPIENT_TYPE)))
                    .setMessage(getString(o, MEMBER_MESSAGE))
                    .setComment(getString(o, MEMBER_COMMENT))
                    .setCodepro(getBoolean(o, MEMBER_CODEPRO))
                    .setProtectionCode(getString(o, MEMBER_PROTECTION_CODE))
                    .setExpires(getDateTime(o, MEMBER_EXPIRES))
                    .setAnswerDatetime(getDateTime(o, MEMBER_ANSWER_DATETIME))
                    .setDetails(getString(o, MEMBER_DETAILS))
                    .setRepeatable(getBoolean(o, MEMBER_REPEATABLE))
                    .setPaymentParameters(getNotNullMap(o, MEMBER_PAYMENT_PARAMETERS))
                    .setFavorite(getBoolean(o, MEMBER_FAVOURITE))
                    .setDigitalGoods(DigitalGoodsTypeAdapter.getInstance().fromJson(o.get(
                            MEMBER_DIGITAL_GOODS)))
                    .setCategories(GsonProvider.getGson().fromJson(o.getAsJsonArray(MEMBER_CATEGORIES), listType))
                    .setFormat(ShowcaseReference.Format.parse(getString(o, MEMBER_FORMAT)))
                    .create();
        } catch (ParseException e) {
            throw new JsonParseException(e);
        }
    }

    @Override
    public JsonElement serialize(Operation src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        object.addProperty(MEMBER_OPERATION_ID, src.operationId);
        object.addProperty(MEMBER_TITLE, src.title);
        object.addProperty(MEMBER_DIRECTION, src.direction.code);
        object.addProperty(MEMBER_DATETIME, Iso8601Format.format(src.datetime));
        object.addProperty(MEMBER_STATUS, src.status.code);
        object.addProperty(MEMBER_PATTERN_ID, src.patternId);
        object.addProperty(MEMBER_AMOUNT, src.amount);
        object.addProperty(MEMBER_AMOUNT_DUE, src.amountDue);
        object.addProperty(MEMBER_FEE, src.fee);
        object.addProperty(MEMBER_LABEL, src.label);
        object.addProperty(MEMBER_TYPE, src.type.code);
        object.addProperty(MEMBER_SENDER, src.sender);
        object.addProperty(MEMBER_RECIPIENT, src.recipient);
        if (src.recipientType != null) {
            object.addProperty(MEMBER_RECIPIENT_TYPE, src.recipientType.code);
        }
        object.addProperty(MEMBER_MESSAGE, src.message);
        object.addProperty(MEMBER_COMMENT, src.comment);
        object.addProperty(MEMBER_CODEPRO, src.codepro);
        object.addProperty(MEMBER_PROTECTION_CODE, src.protectionCode);
        if (src.expires != null) {
            object.addProperty(MEMBER_EXPIRES, Iso8601Format.format(src.expires));
        }
        if (src.answerDatetime != null) {
            object.addProperty(MEMBER_ANSWER_DATETIME, Iso8601Format.format(src.answerDatetime));
        }
        object.addProperty(MEMBER_DETAILS, src.details);
        object.addProperty(MEMBER_REPEATABLE, src.repeatable);
        if (src.paymentParameters.size() > 0) {
            object.add(MEMBER_PAYMENT_PARAMETERS, toJsonObject(src.paymentParameters));
        }
        object.addProperty(MEMBER_FAVOURITE, src.favorite);
        if (src.digitalGoods != null) {
            object.add(MEMBER_DIGITAL_GOODS, DigitalGoodsTypeAdapter.getInstance().toJsonTree(src.digitalGoods));
        }
        if (!src.categories.isEmpty()) {
            object.add(MEMBER_CATEGORIES, GsonProvider.getGson().toJsonTree(src.categories));
        }
        if (src.format != null) {
            object.addProperty(MEMBER_FORMAT, src.format.code);
        }
        return object;
    }

    @Override
    protected Class<Operation> getType() {
        return Operation.class;
    }
}
