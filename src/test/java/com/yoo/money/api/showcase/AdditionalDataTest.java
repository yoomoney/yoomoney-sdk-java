package com.yoo.money.api.showcase;

import com.yoo.money.api.model.showcase.components.uicontrols.AdditionalData;

import org.junit.Assert;
import org.junit.Test;

public class AdditionalDataTest extends ParameterTest {

    @Test
    public void testValidation() {
        AdditionalData.Builder builder = new AdditionalData.Builder();
        prepareParameter(builder);

        AdditionalData additionalData = builder.create();
        Assert.assertTrue(additionalData.isValid());

        testEmptyValues(builder);
    }
}
