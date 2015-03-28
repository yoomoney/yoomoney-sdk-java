package com.yandex.money.api.processes;

import com.yandex.money.api.methods.BaseProcessPayment;
import com.yandex.money.api.methods.BaseRequestPayment;
import com.yandex.money.api.model.MoneySource;

import java.util.Map;

/**
 * Interface for all payment processes.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
interface IPaymentProcess extends Process {

    /**
     * Resets payment process to its initial state.
     */
    void reset();

    /**
     * @return base request payment object
     */
    BaseRequestPayment getRequestPayment();

    /**
     * @return base process payment object
     */
    BaseProcessPayment getProcessPayment();

    /**
     * @param callback callback to perform on operation complete
     */
    void setCallback(Callback callback);

    /**
     * Provides parameters to perform payment process.
     */
    interface ParameterProvider {

        /**
         * @return pattern id
         */
        String getPatternId();

        /**
         * @return key-value pairs of payment parameters
         */
        Map<String, String> getPaymentParameters();

        /**
         * @return selected money source
         */
        MoneySource getMoneySource();

        /**
         * @return CSC code if required
         */
        String getCsc();

        /**
         * @return success URI
         */
        String getExtAuthSuccessUri();

        /**
         * @return fail URI
         */
        String getExtAuthFailUri();
    }

    /**
     * Callback to call on operation complete.
     */
    interface Callback {

        /**
         * Called when request payment response received.
         */
        void onRequestPayment();

        /**
         * Called when process payment response received.
         */
        void onProcessPayment();
    }
}
