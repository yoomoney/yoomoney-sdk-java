package com.yandex.money.test;

import com.yandex.money.api.YandexMoney;
import com.yandex.money.api.exceptions.InsufficientScopeException;
import com.yandex.money.api.exceptions.InvalidRequestException;
import com.yandex.money.api.exceptions.InvalidTokenException;
import com.yandex.money.api.methods.OperationDetails;
import com.yandex.money.api.methods.OperationHistory;
import com.yandex.money.api.model.Operation;

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

    private YandexMoney yandexMoney;

    @BeforeTest
    public void beforeTest() {
        yandexMoney = new YandexMoney(CLIENT_ID);
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
        }

        if (operations.size() > 0) {
            HashSet<OperationHistory.FilterType> types = new HashSet<>();
            types.add(OperationHistory.FilterType.PAYMENT);
            types.add(OperationHistory.FilterType.DEPOSITION);

            historyRequest = new OperationHistory.Request.Builder()
                    .setTypes(types)
                    .setFrom(operations.get(operations.size() - 1).getDatetime())
                    .setTill(operations.get(0).getDatetime().plusSeconds(1))
                    .setDetails(true)
                    .setRecords(operations.size())
                    .createRequest();

            yandexMoney.setDebugLogging(true);
            Assert.assertEquals(yandexMoney.execute(historyRequest).getOperations().size(),
                    operations.size());
        }
    }
}
