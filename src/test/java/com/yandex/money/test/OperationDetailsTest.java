package com.yandex.money.test;

import com.yandex.money.YandexMoney;
import com.yandex.money.exceptions.InsufficientScopeException;
import com.yandex.money.exceptions.InvalidRequestException;
import com.yandex.money.exceptions.InvalidTokenException;
import com.yandex.money.model.cps.OperationDetails;
import com.yandex.money.model.cps.OperationHistory;
import com.yandex.money.model.cps.misc.Operation;

import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class OperationDetailsTest implements ApiTest {

    private YandexMoney yandexMoney;

    @BeforeTest
    public void beforeTest() {
        yandexMoney = new YandexMoney();
        yandexMoney.setDebugLogging(true);
        yandexMoney.setAccessToken(ACCESS_TOKEN);
    }

    @Test
    public void testOperationDetails() throws InvalidTokenException, InsufficientScopeException,
            InvalidRequestException, IOException {

        OperationHistory.Request historyRequest = new OperationHistory.Request.Builder()
                .createRequest();
        OperationHistory history = yandexMoney.execute(historyRequest);
        Assert.assertNotNull(history);

        DateTime from = null;
        DateTime till = null;
        yandexMoney.setDebugLogging(false);
        List<Operation> operations = history.getOperations();
        for (Operation operation : operations) {
            Assert.assertNotNull(operation);
            Assert.assertNotNull(operation.getOperationId());
            OperationDetails.Request detailsRequest = new OperationDetails.Request(
                    operation.getOperationId());
            OperationDetails operationDetails = yandexMoney.execute(detailsRequest);
            Assert.assertNotNull(operationDetails);
            Assert.assertNotNull(operationDetails.getOperation());
            Assert.assertNull(operationDetails.getError());
            Assert.assertEquals(operation.getOperationId(),
                    operationDetails.getOperation().getOperationId());

            DateTime datetime = operation.getDatetime();
            Assert.assertNotNull(datetime);
            Assert.assertTrue(datetime.isEqual(operationDetails.getOperation().getDatetime()));

            if (from == null) {
                from = datetime;
                till = datetime;
            } else {
                if (from.isAfter(datetime)) {
                    from = datetime.minusSeconds(1);
                }
                if (till.isBefore(datetime)) {
                    till = datetime.plusSeconds(1);
                }
            }
        }

        if (operations.size() > 1) {
            Assert.assertNotEquals(from, till);
        }

        historyRequest = new OperationHistory.Request.Builder()
                .setFrom(from)
                .setTill(till)
                .createRequest();
        yandexMoney.setDebugLogging(true);
        Assert.assertEquals(yandexMoney.execute(historyRequest).getOperations().size(),
                operations.size());
    }
}
