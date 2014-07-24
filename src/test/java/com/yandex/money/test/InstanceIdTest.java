package com.yandex.money.test;

import com.yandex.money.model.methods.Error;
import com.yandex.money.model.methods.InstanceId;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 */
public class InstanceIdTest {

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testRequestClientIdNull() {
        new InstanceId.Request(null);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testRequestClientIdEmpty() {
        new InstanceId.Request("");
    }

    @Test()
    public void testRequestClient() {
        InstanceId.Request request = new InstanceId.Request(" ");
        Assert.assertNotNull(request);
    }

    @Test
    public void testIsSuccess() {
        InstanceId instanceId = new InstanceId(InstanceId.Status.SUCCESS, null, "id");
        Assert.assertTrue(instanceId.isSuccess());

        instanceId = new InstanceId(InstanceId.Status.REFUSED, Error.UNKNOWN, null);
        Assert.assertFalse(instanceId.isSuccess());
    }
}
