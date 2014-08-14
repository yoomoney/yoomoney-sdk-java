package com.yandex.money.methods.params;

import com.yandex.money.utils.Strings;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class P2pParams implements Params {

    public static final String PATTERN_ID = "p2p";

    private static final String PARAM_TO = "to";
    private static final String PARAM_AMOUNT_DUE = "amount_due";
    private static final String PARAM_MESSAGE = "message";

    private final String to;
    private final BigDecimal amountDue;
    private final String message;

    public P2pParams(String to, BigDecimal amountDue, String message) {
        if (Strings.isNullOrEmpty(to))
            throw new IllegalArgumentException(PARAM_TO + " is null or empty");
        this.to = to;

        if (amountDue == null)
            throw new IllegalArgumentException(PARAM_AMOUNT_DUE + " is null or empty");
        this.amountDue = amountDue;

        this.message = message;
    }

    public P2pParams(String to, BigDecimal amountDue) {
        this(to, amountDue, null);
    }

    public Map<String, String> makeParams() {
        Map<String, String> result = new HashMap<String, String>();
        result.put(PARAM_TO, to);

        result.put(PARAM_AMOUNT_DUE, amountDue.toPlainString());

        if (Strings.isNullOrEmpty(message)) {
            result.put(PARAM_MESSAGE, message);
        }

        return result;
    }
}
