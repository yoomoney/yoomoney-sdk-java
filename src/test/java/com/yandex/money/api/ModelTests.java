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

package com.yandex.money.api;

import com.google.gson.JsonParser;
import com.yandex.money.api.methods.IncomingTransferAccept;
import com.yandex.money.api.methods.IncomingTransferReject;
import com.yandex.money.api.methods.InstanceId;
import com.yandex.money.api.model.BalanceDetails;
import com.yandex.money.api.model.Card;
import com.yandex.money.api.model.Error;
import com.yandex.money.api.model.ExternalCard;
import com.yandex.money.api.model.SimpleStatus;
import com.yandex.money.api.model.StatusInfo;
import com.yandex.money.api.model.showcase.AmountType;
import com.yandex.money.api.model.showcase.CustomFee;
import com.yandex.money.api.model.showcase.NoFee;
import com.yandex.money.api.model.showcase.StdFee;
import com.yandex.money.api.typeadapters.TypeAdapter;
import com.yandex.money.api.typeadapters.methods.AccountInfoTypeAdapter;
import com.yandex.money.api.typeadapters.methods.IncomingTransferAcceptTypeAdapter;
import com.yandex.money.api.typeadapters.methods.IncomingTransferRejectTypeAdapter;
import com.yandex.money.api.typeadapters.methods.InstanceIdTypeAdapter;
import com.yandex.money.api.typeadapters.methods.OperationDetailsTypeAdapter;
import com.yandex.money.api.typeadapters.methods.OperationHistoryTypeAdapter;
import com.yandex.money.api.typeadapters.methods.RequestExternalPaymentTypeAdapter;
import com.yandex.money.api.typeadapters.methods.RequestPaymentTypeAdapter;
import com.yandex.money.api.typeadapters.model.BalanceDetailsTypeAdapter;
import com.yandex.money.api.typeadapters.model.CardTypeAdapter;
import com.yandex.money.api.typeadapters.model.ErrorTypeAdapter;
import com.yandex.money.api.typeadapters.model.ExternalCardTypeAdapter;
import com.yandex.money.api.typeadapters.model.StatusInfoTypeAdapter;
import com.yandex.money.api.typeadapters.model.showcase.FeeTypeAdapter;
import com.yandex.money.api.typeadapters.model.showcase.ShowcaseTypeAdapter;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.math.BigDecimal;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class ModelTests {

    @Test
    public void testAccountInfo() {
        checkTypeAdapter("/methods/account-info.json", AccountInfoTypeAdapter.getInstance());
    }

    @Test
    public void testBalanceDetails() {
        performTest(createBalanceDetails(), BalanceDetailsTypeAdapter.getInstance());
    }

    @Test
    public void testCard() {
        performTest(createCard(), CardTypeAdapter.getInstance());
    }

    @Test
    public void testExternalCard() {
        performTest(createExternalCard(), ExternalCardTypeAdapter.getInstance());
    }

    @Test
    public void testIncomingTransferAccept() {
        IncomingTransferAcceptTypeAdapter adapter = IncomingTransferAcceptTypeAdapter.getInstance();

        checkTypeAdapter("/methods/incoming-transfer-accept-success.json", adapter);
        checkTypeAdapter("/methods/incoming-transfer-accept-refused-1.json", adapter);
        checkTypeAdapter("/methods/incoming-transfer-accept-refused-2.json", adapter);

        performTest(new IncomingTransferAccept(StatusInfo.from(SimpleStatus.SUCCESS, null), null, null), adapter);
        performTest(new IncomingTransferAccept(StatusInfo.from(SimpleStatus.REFUSED, Error.TECHNICAL_ERROR), null, null),
                adapter);
    }

    @Test
    public void testIncomingTransferReject() {
        IncomingTransferRejectTypeAdapter adapter = IncomingTransferRejectTypeAdapter.getInstance();

        checkTypeAdapter("/methods/incoming-transfer-reject-success.json", adapter);
        checkTypeAdapter("/methods/incoming-transfer-reject-refused.json", adapter);

        performTest(new IncomingTransferReject(StatusInfo.from(SimpleStatus.SUCCESS, null)), adapter);
        performTest(new IncomingTransferReject(StatusInfo.from(SimpleStatus.REFUSED, Error.TECHNICAL_ERROR)), adapter);
    }

    @Test
    public void testInstanceId() {
        InstanceIdTypeAdapter adapter = InstanceIdTypeAdapter.getInstance();

        checkTypeAdapter("/methods/instance-id-success.json", adapter);
        checkTypeAdapter("/methods/instance-id-refused.json", adapter);

        performTest(new InstanceId(StatusInfo.from(SimpleStatus.SUCCESS, null), "123"), adapter);
        performTest(new InstanceId(StatusInfo.from(SimpleStatus.REFUSED, Error.TECHNICAL_ERROR), null), adapter);
    }

    @Test
    public void testOperationDetails() {
        OperationDetailsTypeAdapter adapter = OperationDetailsTypeAdapter.getInstance();
        checkTypeAdapter("/methods/operation-details-1.json", adapter);
        checkTypeAdapter("/methods/operation-details-2.json", adapter);
        checkTypeAdapter("/methods/operation-details-3.json", adapter);
    }

    @Test
    public void testOperationHistory() {
        OperationHistoryTypeAdapter adapter = OperationHistoryTypeAdapter.getInstance();
        checkTypeAdapter("/methods/operation-history-1.json", adapter);
        checkTypeAdapter("/methods/operation-history-2.json", adapter);
        checkTypeAdapter("/methods/operation-history-3.json", adapter);
        checkTypeAdapter("/methods/operation-history-4.json", adapter);
    }

    @Test
    public void testRequestExternalPayment() {
        RequestExternalPaymentTypeAdapter adapter = RequestExternalPaymentTypeAdapter.getInstance();
        checkTypeAdapter("/methods/request-external-payment-1.json", adapter);
        checkTypeAdapter("/methods/request-external-payment-2.json", adapter);
    }

    @Test
    public void testRequestPayment() {
        RequestPaymentTypeAdapter adapter = RequestPaymentTypeAdapter.getInstance();
        checkTypeAdapter("/methods/request-payment-1.json", adapter);
        checkTypeAdapter("/methods/request-payment-2.json", adapter);
    }

    @Test
    public void testShowcase() {
        ShowcaseTypeAdapter adapter = ShowcaseTypeAdapter.getInstance();
        checkTypeAdapter("/showcase/showcase-1.json", adapter);
    }

    @Test
    public void testError() {
        performTest(Error.TECHNICAL_ERROR, ErrorTypeAdapter.getInstance());
    }

    @Test
    public void testFee() {
        performTest(new StdFee(BigDecimal.TEN, BigDecimal.ONE, BigDecimal.ZERO, null,
                AmountType.NET_AMOUNT), FeeTypeAdapter.getInstance());
    }

    @Test
    public void testNoFee() {
        try {
            performTest(NoFee.getInstance(), FeeTypeAdapter.getInstance());
            Assert.fail();
        } catch (NullPointerException e) {
            // does nothing
        }
    }

    @Test
    public void testStatusInfo() {
        StatusInfoTypeAdapter adapter = StatusInfoTypeAdapter.getInstance();
        checkTypeAdapter("/model/status-info-1.json", adapter);
        checkTypeAdapter("/model/status-info-2.json", adapter);
    }

    /**
     * Reads JSON path and asserts it for equality after deserialization and serialization steps.
     *
     * @param path JSON path name in resources
     * @param typeAdapter type adapter to check
     */
    static <T> void checkTypeAdapter(String path, TypeAdapter<T> typeAdapter) {
        try {
            String json = Resources.load(path);
            T deserializedObject = typeAdapter.fromJson(json);
            assertNotEquals(0, deserializedObject.hashCode());
            assertEquals(typeAdapter.toJsonTree(deserializedObject), new JsonParser().parse(json));
        } catch (FileNotFoundException e) {
            assertFalse(true);
        }
    }

    private static <T> void performTest(T value, TypeAdapter<T> adapter) {
        assertEquals(adapter.fromJson(adapter.toJsonTree(value)), value);
    }

    private static BalanceDetails createBalanceDetails() {
        return new BalanceDetails.Builder()
                .setTotal(BigDecimal.ONE)
                .setAvailable(BigDecimal.TEN)
                .create();
    }

    private static Card createCard() {
        Card.Builder builder = (Card.Builder) new Card.Builder()
                .setPanFragment("panFragment")
                .setType(Card.Type.MASTER_CARD)
                .setId("id");
        return builder.create();
    }

    private static ExternalCard createExternalCard() {
        ExternalCard.Builder builder = (ExternalCard.Builder) new ExternalCard.Builder()
                .setFundingSourceType("fundingSourceType")
                .setMoneySourceToken("moneySourceToken")
                .setType(Card.Type.AMERICAN_EXPRESS)
                .setPanFragment("1234 56** **** 7890");
        return builder.create();
    }

    @Test
    void testCustomFee() {
        performTest(CustomFee.getInstance(), FeeTypeAdapter.getInstance());
    }
}
