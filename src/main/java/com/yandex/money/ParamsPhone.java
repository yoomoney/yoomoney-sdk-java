package com.yandex.money;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ParamsPhone implements Params {

    public static final String PATTERN_ID = "phone-topup";

    private final static String PARAM_PHONE_NUMBER = "phone-number";

    private String number;

    public ParamsPhone(String number) {
        if (Utils.isEmpty(number))
            throw new IllegalArgumentException(Utils.emptyParam(PARAM_PHONE_NUMBER));
        this.number = number;
    }


    @Override
    public Map<String, String> makeParams() {
        Map<String, String> result = new HashMap<String, String>();
        result.put(PARAM_PHONE_NUMBER, number);
        return result;
    }


}
