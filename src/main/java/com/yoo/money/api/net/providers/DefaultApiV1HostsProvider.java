/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 NBCO YooMoney LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.yoo.money.api.net.providers;

/**
 * Provides necessary hosts. They are used to perform API requests.
 *
 * @author Slava Yasevich (support@yoomoney.ru)
 */
public class DefaultApiV1HostsProvider implements HostsProvider {

    private final boolean mobile;

    /**
     * Constructor.
     *
     * @param mobile {@code true} if running on a mobile device
     */
    public DefaultApiV1HostsProvider(boolean mobile) {
        this.mobile = mobile;
    }

    /**
     * @return {@code https://yoomoney.ru}
     */
    @Override
    public String getMoney() {
        return "https://yoomoney.ru";
    }

    /**
     * @return {@code https://yoomoney.ru/api}
     */
    @Override
    public String getMoneyApi() {
        return getMoney() + "/api";
    }

    /**
     * @return {@code https://yoomoney.ru/api}
     */
    @Override
    public String getPaymentApi() {
        return getMoneyApi();
    }

    /**
     * @return {@code https://m.yoomoney.ru}
     */
    @Override
    public String getMobileMoney() {
        return "https://m.yoomoney.ru";
    }

    /**
     * @return platform specific url
     */
    @Override
    public final String getWebUrl() {
        return mobile ? getMobileMoney() : getMoney();
    }
}