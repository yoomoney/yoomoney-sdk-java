package com.yandex.money;

import java.math.BigDecimal;

/**
 *
 */
public class Utils {

    public static String NULL_PARAM_STR = "parameter %s is null or empty";

    public static String emptyParam(String paramName) {
        return String.format(NULL_PARAM_STR, paramName);
    }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static String sumToStr(BigDecimal amountDue) {
        return amountDue.toPlainString();
    }
}
