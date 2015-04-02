package com.yandex.money.api.methods.params;

import java.util.Map;

/**
 * @author Dmitriy Melnikov (dvmelnikov@yamoney.ru)
 */
public interface Params {

    /**
     * Returns pattern id associated with these parameters.
     *
     * @return pattern id
     */
    String getPatternId();

    /**
     * Creates parameters for {@link com.yandex.money.api.methods.RequestPayment.Request}.
     *
     * @return key-value pairs of parameters
     */
    Map<String, String> makeParams();
}
