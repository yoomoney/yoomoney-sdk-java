/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 NBCO Yandex.Money LLC
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

package com.yandex.money.api.methods;

import com.yandex.money.api.model.AccountStatus;
import com.yandex.money.api.model.AccountType;
import com.yandex.money.api.model.Avatar;
import com.yandex.money.api.model.BalanceDetails;
import com.yandex.money.api.model.Card;
import com.yandex.money.api.net.HostsProvider;
import com.yandex.money.api.net.MethodResponse;
import com.yandex.money.api.net.PostRequest;
import com.yandex.money.api.typeadapters.AccountInfoTypeAdapter;
import com.yandex.money.api.utils.Currency;
import com.yandex.money.api.utils.Strings;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Information of user account.
 *
 * @author Roman Tsirulnikov (romanvt@yamoney.ru)
 */
public class AccountInfo implements MethodResponse {

    public final String account;
    public final BigDecimal balance;
    public final Currency currency;
    public final AccountStatus accountStatus;
    public final AccountType accountType;
    public final Avatar avatar;
    public final BalanceDetails balanceDetails;
    public final List<Card> linkedCards;
    public final List<String> additionalServices;

    /**
     * Constructor.
     *
     * @param account account's number (required)
     * @param balance current balance (required)
     * @param currency selected currency
     * @param accountStatus account's status
     * @param accountType account's type
     * @param avatar avatar's info
     * @param balanceDetails detailed balance if available
     * @param linkedCards linked cards
     * @param additionalServices additional services
     */
    public AccountInfo(String account, BigDecimal balance, Currency currency,
                       AccountStatus accountStatus, AccountType accountType, Avatar avatar,
                       BalanceDetails balanceDetails, List<Card> linkedCards,
                       List<String> additionalServices) {

        if (Strings.isNullOrEmpty(account)) {
            throw new IllegalArgumentException("account is null or empty");
        }
        if (balance == null) {
            throw new NullPointerException("balance is null");
        }
        if (currency == null) {
            throw new NullPointerException("currency is null");
        }
        if (accountStatus == null) {
            throw new NullPointerException("accountStatus is null");
        }
        if (accountType == null) {
            throw new NullPointerException("accountType is null");
        }
        if (balanceDetails == null) {
            throw new NullPointerException("balanceDetails is null");
        }
        if (linkedCards == null) {
            throw new NullPointerException("linkedCards is null");
        }
        if (additionalServices == null) {
            throw new NullPointerException("additionalServices is null");
        }
        this.account = account;
        this.balance = balance;
        this.currency = currency;
        this.accountStatus = accountStatus;
        this.accountType = accountType;
        this.avatar = avatar;
        this.balanceDetails = balanceDetails;
        this.linkedCards = Collections.unmodifiableList(linkedCards);
        this.additionalServices = Collections.unmodifiableList(additionalServices);
    }

    @Override
    public String toString() {
        return "AccountInfo{" +
                "account='" + account + '\'' +
                ", balance=" + balance +
                ", currency=" + currency +
                ", accountStatus=" + accountStatus +
                ", accountType=" + accountType +
                ", avatar=" + avatar +
                ", balanceDetails=" + balanceDetails +
                ", linkedCards=" + linkedCards +
                ", additionalServices=" + additionalServices +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof AccountInfo) {
            AccountInfo info = (AccountInfo) obj;
            return account.equals(info.account) && balance.equals(info.balance) &&
                    currency == info.currency && accountStatus == info.accountStatus &&
                    accountType == info.accountType && Objects.equals(avatar, info.avatar) &&
                    balanceDetails.equals(info.balanceDetails) &&
                    linkedCards.equals(info.linkedCards) &&
                    additionalServices.equals(info.additionalServices);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, balance, currency, accountStatus, accountType, avatar,
                balanceDetails, linkedCards, additionalServices);
    }

    public boolean isIdentified() {
        return accountStatus == AccountStatus.IDENTIFIED;
    }

    /**
     * Requests for {@link com.yandex.money.api.methods.AccountInfo}.
     * <p/>
     * Authorized session required.
     *
     * @see com.yandex.money.api.net.OAuth2Session
     */
    public static final class Request extends PostRequest<AccountInfo> {

        public Request() {
            super(AccountInfo.class, AccountInfoTypeAdapter.getInstance());
        }

        @Override
        public String requestUrl(HostsProvider hostsProvider) {
            return hostsProvider.getMoneyApi() + "/account-info";
        }
    }
}
