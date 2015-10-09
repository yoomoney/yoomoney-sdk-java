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

package com.yandex.money.test;

import com.yandex.money.api.exceptions.InsufficientScopeException;
import com.yandex.money.api.exceptions.InvalidRequestException;
import com.yandex.money.api.exceptions.InvalidTokenException;
import com.yandex.money.api.methods.OperationDetails;
import com.yandex.money.api.methods.OperationHistory;
import com.yandex.money.api.model.Operation;
import com.yandex.money.api.net.DefaultApiClient;
import com.yandex.money.api.net.OAuth2Session;

import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class OperationDetailsTest implements ApiTest {

    private OAuth2Session session;

    @BeforeTest
    public void beforeTest() {
        session = new OAuth2Session(new DefaultApiClient(CLIENT_ID));
        session.setDebugLogging(true);
        session.setAccessToken(ACCESS_TOKEN);
    }

    @Test
    public void testOperationDetails() throws InvalidTokenException, InsufficientScopeException,
            InvalidRequestException, IOException {

        OperationHistory.Request historyRequest = new OperationHistory.Request.Builder()
                .create();
        OperationHistory history = session.execute(historyRequest);
        Assert.assertNotNull(history);

        session.setDebugLogging(false);
        List<Operation> operations = history.operations;
        for (Operation operation : operations) {
            Assert.assertNotNull(operation);
            Assert.assertNotNull(operation.operationId);
            OperationDetails.Request detailsRequest = new OperationDetails.Request(
                    operation.operationId);
            OperationDetails operationDetails = session.execute(detailsRequest);
            Assert.assertNotNull(operationDetails);
            Assert.assertNotNull(operationDetails.operation);
            Assert.assertNull(operationDetails.error);
            Assert.assertEquals(operation.operationId, operationDetails.operation.operationId);

            DateTime datetime = operation.datetime;
            Assert.assertNotNull(datetime);
            Assert.assertTrue(datetime.isEqual(operationDetails.operation.datetime));
        }

        if (operations.size() > 0) {
            HashSet<OperationHistory.FilterType> types = new HashSet<>();
            types.add(OperationHistory.FilterType.PAYMENT);
            types.add(OperationHistory.FilterType.DEPOSITION);

            historyRequest = new OperationHistory.Request.Builder()
                    .setTypes(types)
                    .setFrom(operations.get(operations.size() - 1).datetime)
                    .setTill(operations.get(0).datetime.plusSeconds(1))
                    .setDetails(true)
                    .setRecords(operations.size())
                    .create();

            session.setDebugLogging(true);
            Assert.assertEquals(session.execute(historyRequest).operations.size(),
                    operations.size());
        }
    }
}
