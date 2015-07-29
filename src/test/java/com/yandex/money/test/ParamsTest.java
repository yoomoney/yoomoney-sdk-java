package com.yandex.money.test;

import com.yandex.money.api.methods.params.P2pTransferParams;
import com.yandex.money.api.methods.params.PhoneParams;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Anton Ermak (ermak@yamoney.ru).
 */
public final class ParamsTest {

    private static final String accountNumber = "4100414141414";
    private static final BigDecimal amount = new BigDecimal("10.01");

    /**
     * Tests that {@code amount} or {@code amount_due} is required.
     */
    @Test(expectedExceptions = IllegalStateException.class)
    public void testP2pTransferParamsBuilderException() {
        new P2pTransferParams.Builder(accountNumber).build();
    }

    /**
     * Tests that {@code amount} and {@code to} is required for p2p payment.
     */
    @Test
    public void testP2pTransferParamsBuilderCreated() {
        createP2pBuilderRequired().build();
    }

    /**
     * Tests that key-value naming of p2p params matches documentation.
     */
    @Test
    public void testP2pTransferParamsComplete() throws Exception {
        String comment = "Some comment";
        Integer expirePeriod = 5;
        String label = "some label";
        String message = "message";

        Map<String, String> params = createP2pBuilderRequired()
                .setCodepro(true)
                .setComment(comment)
                .setExpirePeriod(expirePeriod)
                .setLabel(label)
                .setMessage(message)
                .build()
                .makeParams();

        HashMap<String, String> expectedParams = new HashMap<>();
        expectedParams.put("to", accountNumber);
        expectedParams.put("label", label);
        expectedParams.put("amount", amount.toPlainString());
        expectedParams.put("codepro", "true");
        expectedParams.put("message", message);
        expectedParams.put("comment", comment);
        expectedParams.put("expire_period", expirePeriod.toString());

        Assert.assertEquals(params, expectedParams);
    }

    /**
     * Tests that key-value naming of phone topup params matches documentation.
     */
    @Test
    public void testShowParamsComplete() throws Exception {
        String phoneNumber = "79111111111";
        PhoneParams phoneParams = PhoneParams.newInstance(phoneNumber, amount);

        HashMap<String, String> expectedParams = new HashMap<>();
        expectedParams.put("phone-number", phoneNumber);
        expectedParams.put("amount", amount.toPlainString());
        Assert.assertEquals(phoneParams.makeParams(), expectedParams);
    }

    private static P2pTransferParams.Builder createP2pBuilderRequired() {
        return new P2pTransferParams.Builder(accountNumber).setAmount(amount);
    }
}
