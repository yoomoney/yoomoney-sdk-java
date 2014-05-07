package com.yandex.money.model.common.params;

import com.yandex.money.Utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ParamsP2P implements Params {

    public static final String PATTERN_ID = "p2p";

    private static final String PARAM_TO = "to";
    private static final String PARAM_AMOUNT_DUE = "amount_due";
    private static final String PARAM_MESSAGE = "message";

    private final String to;
    private final BigDecimal amountDue;
    private final String message;

    public ParamsP2P(String to, BigDecimal amountDue, String message) {
        if (Utils.isEmpty(to))
            throw new IllegalArgumentException(Utils.emptyParam(PARAM_TO));
        this.to = to;

        if (amountDue == null)
            throw new IllegalArgumentException(Utils.emptyParam(PARAM_AMOUNT_DUE));
        this.amountDue = amountDue;

        this.message = message;
    }

    public ParamsP2P(String to, BigDecimal amountDue) {
        this(to, amountDue, null);
    }

    public Map<String, String> makeParams() {
        Map<String, String> result = new HashMap<String, String>();
        result.put(PARAM_TO, to);

        result.put(PARAM_AMOUNT_DUE, Utils.sumToStr(amountDue));

        if (!Utils.isEmpty(message)) {
            result.put(PARAM_MESSAGE, message);
        }

        return result;
    }
}
