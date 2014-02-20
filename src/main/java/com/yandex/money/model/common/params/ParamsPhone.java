package com.yandex.money.model.common.params;

import com.yandex.money.Utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ParamsPhone implements Params {

    public static final String PATTERN_ID = "phone-topup";

    private final static String PARAM_PHONE_NUMBER = "phone-number";
    private static final String PARAM_AMOUNT = "amount";

    private String number;
    private BigDecimal amount;

    public ParamsPhone(String number, BigDecimal amount) {
        if (Utils.isEmpty(number))
            throw new IllegalArgumentException(Utils.emptyParam(PARAM_PHONE_NUMBER));
        this.number = number;

        if (amount == null)
            throw new IllegalArgumentException(Utils.emptyParam(PARAM_AMOUNT));
        this.amount = amount;
    }


    @Override
    public Map<String, String> makeParams() {
        Map<String, String> result = new HashMap<String, String>();
        result.put(PARAM_PHONE_NUMBER, number);
        result.put(PARAM_AMOUNT, Utils.sumToStr(amount));
        return result;
    }


}
