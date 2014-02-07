package com.yandex.money.test;

import com.yandex.money.model.InstanceId;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 */
public class TestInstanceId {

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
}
