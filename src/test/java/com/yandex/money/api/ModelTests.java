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

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.yandex.money.api.methods.InstanceId;
import com.yandex.money.api.methods.payment.RequestExternalPayment;
import com.yandex.money.api.methods.payment.RequestPayment;
import com.yandex.money.api.methods.wallet.AccountInfo;
import com.yandex.money.api.methods.wallet.IncomingTransferAccept;
import com.yandex.money.api.methods.wallet.IncomingTransferReject;
import com.yandex.money.api.methods.wallet.OperationDetails;
import com.yandex.money.api.methods.wallet.OperationHistory;
import com.yandex.money.api.model.BalanceDetails;
import com.yandex.money.api.model.Card;
import com.yandex.money.api.model.Error;
import com.yandex.money.api.model.ExternalCard;
import com.yandex.money.api.model.Fees;
import com.yandex.money.api.typeadapters.GsonProvider;
import com.yandex.money.api.typeadapters.TypeAdapter;
import com.yandex.money.api.typeadapters.model.showcase.ShowcaseTypeAdapter;

import org.testng.annotations.Test;

import java.io.FileNotFoundException;
import java.math.BigDecimal;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.fail;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class ModelTests {

    @Test
    public void testAccountInfo() {
        checkType("/methods/wallet/account-info.json", AccountInfo.class);
    }

    @Test
    public void testAccountInfoNoBonusBalance() {
        checkType("/methods/wallet/account-info-no-bonus.json", AccountInfo.class);
    }

    @Test
    public void testBalanceDetails() {
        performTest(createBalanceDetails(), BalanceDetails.class);
    }

    @Test
    public void testCard() {
        checkType("/model/card-1.json", Card.class);
        checkType("/model/card-2.json", Card.class);
    }

    @Test
    public void testExternalCard() {
        checkType("/model/external-card.json", ExternalCard.class);
    }

    @Test
    public void testFees() throws Exception {
        checkType("/model/fees-1.json", Fees.class);
        checkType("/model/fees-2.json", Fees.class);
        checkType("/model/fees-3.json", Fees.class);
    }

    @Test
    public void testIncomingTransferAccept() {
        checkType("/methods/wallet/incoming-transfer-accept-success.json", IncomingTransferAccept.class);
        checkType("/methods/wallet/incoming-transfer-accept-refused-1.json", IncomingTransferAccept.class);
        checkType("/methods/wallet/incoming-transfer-accept-refused-2.json", IncomingTransferAccept.class);
    }

    @Test
    public void testIncomingTransferReject() {
        checkType("/methods/wallet/incoming-transfer-reject-success.json", IncomingTransferReject.class);
        checkType("/methods/wallet/incoming-transfer-reject-refused.json", IncomingTransferReject.class);
    }

    @Test
    public void testInstanceId() {
        checkType("/methods/instance-id-success.json", InstanceId.class);
        checkType("/methods/instance-id-refused.json", InstanceId.class);
    }

    @Test
    public void testOperationDetails() {
        checkType("/methods/wallet/operation-details-1.json", OperationDetails.class);
        checkType("/methods/wallet/operation-details-2.json", OperationDetails.class);
        checkType("/methods/wallet/operation-details-3.json", OperationDetails.class);
        checkType("/methods/wallet/operation-details-4.json", OperationDetails.class);
        checkType("/methods/wallet/operation-details-5.json", OperationDetails.class);
    }

    @Test
    public void testOperationHistory() {
        checkType("/methods/wallet/operation-history-1.json", OperationHistory.class);
        checkType("/methods/wallet/operation-history-2.json", OperationHistory.class);
        checkType("/methods/wallet/operation-history-3.json", OperationHistory.class);
        checkType("/methods/wallet/operation-history-4.json", OperationHistory.class);
        checkType("/methods/wallet/operation-history-5.json", OperationHistory.class);
        checkType("/methods/wallet/operation-history-6.json", OperationHistory.class);
        checkType("/methods/wallet/operation-history-7.json", OperationHistory.class);
    }

    @Test
    public void testRequestExternalPayment() {
        checkType("/methods/payment/request-external-payment-1.json", RequestExternalPayment.class);
        checkType("/methods/payment/request-external-payment-2.json", RequestExternalPayment.class);
        checkType("/methods/payment/request-external-payment-3.json", RequestExternalPayment.class);
    }

    @Test
    public void testRequestPayment() {
        checkType("/methods/payment/request-payment-1.json", RequestPayment.class);
        checkType("/methods/payment/request-payment-2.json", RequestPayment.class);
        checkType("/methods/payment/request-payment-3.json", RequestPayment.class);
    }

    @Test
    public void testShowcase() {
        ShowcaseTypeAdapter adapter = ShowcaseTypeAdapter.getInstance();
        checkTypeAdapter("/showcase/showcase-1.json", adapter);
    }

    @Test
    public void testError() {
        performTest(Error.TECHNICAL_ERROR, Error.class);
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
            fail();
        }
    }

    private static <T> void checkType(String path, Class<T> type) {
        try {
            String json = Resources.load(path);
            Gson gson = GsonProvider.getGson();
            T deserializedObject = gson.fromJson(json, type);
            assertNotEquals(0, deserializedObject.hashCode());
            assertEquals(gson.toJsonTree(deserializedObject), new JsonParser().parse(json));
        } catch (FileNotFoundException e) {
            fail();
        }
    }

    private static <T> void performTest(T value, Class<T> cls) {
        Gson gson = GsonProvider.getGson();
        assertEquals(gson.fromJson(gson.toJson(value), cls), value);
    }

    private static BalanceDetails createBalanceDetails() {
        return new BalanceDetails.Builder()
                .setTotal(BigDecimal.ONE)
                .setAvailable(BigDecimal.TEN)
                .create();
    }
}
