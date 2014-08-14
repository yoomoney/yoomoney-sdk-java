package com.yandex.money.api.methods.params;

import com.yandex.money.api.utils.Strings;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Convenience class for phone top up parameters.
 *
 * @author Dmitriy Melnikov (dvmelnikov@yamoney.ru)
 */
public class PhoneParams implements Params {

    public static final String PATTERN_ID = "phone-topup";

    private static final String PARAM_PHONE_NUMBER = "phone-number";
    private static final String PARAM_AMOUNT = "amount";

    private final String number;
    private final BigDecimal amount;

    /**
     * Constructor.
     *
     * @param number phone number
     * @param amount top up amount
     */
    public PhoneParams(String number, BigDecimal amount) {
        if (Strings.isNullOrEmpty(number))
            throw new IllegalArgumentException(PARAM_PHONE_NUMBER + " is null or empty");
        this.number = number;

        if (amount == null)
            throw new IllegalArgumentException(PARAM_AMOUNT + " is null or empty");
        this.amount = amount;
    }


    @Override
    public Map<String, String> makeParams() {
        Map<String, String> result = new HashMap<String, String>();
        result.put(PARAM_PHONE_NUMBER, number);
        result.put(PARAM_AMOUNT, amount.toPlainString());
        return result;
    }
}
