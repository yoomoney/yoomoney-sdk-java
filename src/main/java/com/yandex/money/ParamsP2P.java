package com.yandex.money;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class ParamsP2P implements Params {

    public static final String PATTERN_ID = "p2p";

    private static final String PARAM_TO = "to";
    private static final String PARAM_TO_TYPE = "toType";
    private static final String PARAM_AMOUNT_DUE = "amountDue";
    private static final String PARAM_MESSAGE = "message";

    String to;
    IdentifierType toType;
    BigDecimal amountDue;
    String message;

    public ParamsP2P(String to, IdentifierType toType, BigDecimal amountDue, String message) {
        if (Utils.isEmpty(to))
            throw new IllegalArgumentException(Utils.emptyParam(PARAM_TO));
        this.to = to;

        if (toType == null)
            throw new IllegalArgumentException(Utils.emptyParam(PARAM_TO_TYPE));
        this.toType = toType;

        if (amountDue == null)
            throw new IllegalArgumentException(Utils.emptyParam(PARAM_AMOUNT_DUE));
        this.amountDue = amountDue;

        this.message = message;
    }

    public ParamsP2P(String to, IdentifierType toType, BigDecimal amountDue) {
        this(to, toType, amountDue, null);
    }

    public Map<String, String> makeParams() {
        Map<String, String> result = new HashMap<String, String>();
        result.put(PARAM_TO, to);

        switch (toType) {
            case ACCOUNT:
                result.put(PARAM_TO_TYPE, "account");
                break;
            case PHONE:
                result.put(PARAM_TO_TYPE, "phone");
                break;
        }

        result.put(PARAM_AMOUNT_DUE, amountDue.toString());  // @todo make a test for values like 1.929299299

        if (!Utils.isEmpty(message)) {
            result.put(PARAM_MESSAGE, message);
        }

        return result;
    }
}
