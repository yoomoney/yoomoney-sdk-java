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
import com.yandex.money.api.model.YandexMoneyCard;
import com.yandex.money.api.net.HostsProvider;
import com.yandex.money.api.net.MethodResponse;
import com.yandex.money.api.net.PostRequest;
import com.yandex.money.api.typeadapters.AccountInfoTypeAdapter;
import com.yandex.money.api.utils.Currency;
import com.yandex.money.api.utils.Strings;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Information of user account.
 *
 * @author Roman Tsirulnikov (romanvt@yamoney.ru)
 */
public class AccountInfo implements MethodResponse {

    /**
     * account number
     */
    public final String account;

    /**
     * current balance
     */
    public final BigDecimal balance;

    /**
     * account's currency
     */
    public final Currency currency;

    /**
     * account's status
     */
    public final AccountStatus accountStatus;

    /**
     * account's type
     */
    public final AccountType accountType;

    /**
     * avatar
     */
    public final Avatar avatar;

    /**
     * balance details
     */
    public final BalanceDetails balanceDetails;

    /**
     * list of linked cards
     */
    public final List<Card> linkedCards;

    /**
     * list of additional services
     */
    public final List<String> additionalServices;

    /**
     * list of Yandex.Money cards
     */
    public final List<YandexMoneyCard> yandexMoneyCards;

    /**
     * @deprecated use {@link com.yandex.money.api.methods.AccountInfo.Builder} instead
     */
    @Deprecated
    public AccountInfo(String account, BigDecimal balance, Currency currency,
                       AccountStatus accountStatus, AccountType accountType, Avatar avatar,
                       BalanceDetails balanceDetails, List<Card> linkedCards,
                       List<String> additionalServices, List<YandexMoneyCard> yandexMoneyCards) {
        this(new Builder()
                .setAccount(account)
                .setBalance(balance)
                .setCurrency(currency)
                .setAccountStatus(accountStatus)
                .setAccountType(accountType)
                .setAvatar(avatar)
                .setBalanceDetails(balanceDetails)
                .setLinkedCards(linkedCards)
                .setAdditionalServices(additionalServices)
                .setYandexMoneyCards(yandexMoneyCards));
    }

    private AccountInfo(Builder builder) {
        if (Strings.isNullOrEmpty(builder.account)) {
            throw new IllegalArgumentException("account is null or empty");
        }
        if (builder.balance == null) {
            throw new NullPointerException("balance is null");
        }
        if (builder.currency == null) {
            throw new NullPointerException("currency is null");
        }
        if (builder.accountStatus == null) {
            throw new NullPointerException("accountStatus is null");
        }
        if (builder.accountType == null) {
            throw new NullPointerException("accountType is null");
        }
        if (builder.balanceDetails == null) {
            throw new NullPointerException("balanceDetails is null");
        }
        if (builder.linkedCards == null) {
            throw new NullPointerException("linkedCards is null");
        }
        if (builder.additionalServices == null) {
            throw new NullPointerException("additionalServices is null");
        }
        if (builder.yandexMoneyCards == null) {
            throw new NullPointerException("yandexMoneyCards is null");
        }
        account = builder.account;
        balance = builder.balance;
        currency = builder.currency;
        accountStatus = builder.accountStatus;
        accountType = builder.accountType;
        avatar = builder.avatar;
        balanceDetails = builder.balanceDetails;
        linkedCards = Collections.unmodifiableList(builder.linkedCards);
        additionalServices = Collections.unmodifiableList(builder.additionalServices);
        yandexMoneyCards = Collections.unmodifiableList(builder.yandexMoneyCards);
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
                ", yandexMoneyCards=" + yandexMoneyCards +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountInfo that = (AccountInfo) o;

        return account.equals(that.account) && balance.equals(that.balance) && currency == that.currency &&
                accountStatus == that.accountStatus && accountType == that.accountType &&
                !(avatar != null ? !avatar.equals(that.avatar) : that.avatar != null) &&
                balanceDetails.equals(that.balanceDetails) && linkedCards.equals(that.linkedCards) &&
                additionalServices.equals(that.additionalServices) && yandexMoneyCards.equals(that.yandexMoneyCards);
    }

    @Override
    public int hashCode() {
        int result = account.hashCode();
        result = 31 * result + balance.hashCode();
        result = 31 * result + currency.hashCode();
        result = 31 * result + accountStatus.hashCode();
        result = 31 * result + accountType.hashCode();
        result = 31 * result + (avatar != null ? avatar.hashCode() : 0);
        result = 31 * result + balanceDetails.hashCode();
        result = 31 * result + linkedCards.hashCode();
        result = 31 * result + additionalServices.hashCode();
        result = 31 * result + yandexMoneyCards.hashCode();
        return result;
    }

    /**
     * @deprecated use {@link #accountStatus} instead
     */
    @Deprecated
    public boolean isIdentified() {
        return accountStatus == AccountStatus.IDENTIFIED;
    }

    /**
     * Creates {@link AccountInfo} instance.
     */
    public static class Builder {

        private String account;
        private BigDecimal balance;
        private Currency currency;
        private AccountStatus accountStatus;
        private AccountType accountType;
        private Avatar avatar;
        private BalanceDetails balanceDetails;
        private List<Card> linkedCards;
        private List<String> additionalServices;
        private List<YandexMoneyCard> yandexMoneyCards;

        /**
         * @param account account's number
         * @return itself
         */
        public Builder setAccount(String account) {
            this.account = account;
            return this;
        }

        /**
         * @param balance current balance
         * @return itself
         */
        public Builder setBalance(BigDecimal balance) {
            this.balance = balance;
            return this;
        }

        /**
         * @param currency account's currency
         * @return itself
         */
        public Builder setCurrency(Currency currency) {
            this.currency = currency;
            return this;
        }

        /**
         * @param accountStatus account's status
         * @return itself
         */
        public Builder setAccountStatus(AccountStatus accountStatus) {
            this.accountStatus = accountStatus;
            return this;
        }

        /**
         * @param accountType account's type
         * @return itself
         */
        public Builder setAccountType(AccountType accountType) {
            this.accountType = accountType;
            return this;
        }

        /**
         * @param avatar avatar
         * @return itself
         */
        public Builder setAvatar(Avatar avatar) {
            this.avatar = avatar;
            return this;
        }

        /**
         * @param balanceDetails balance details
         * @return itself
         */
        public Builder setBalanceDetails(BalanceDetails balanceDetails) {
            this.balanceDetails = balanceDetails;
            return this;
        }

        /**
         * @param linkedCards list of linked cards
         * @return itself
         */
        public Builder setLinkedCards(List<Card> linkedCards) {
            this.linkedCards = linkedCards;
            return this;
        }

        /**
         * @param additionalServices list of additional services
         * @return itself
         */
        public Builder setAdditionalServices(List<String> additionalServices) {
            this.additionalServices = additionalServices;
            return this;
        }

        /**
         * @param yandexMoneyCards list of Yandex.Money cards
         * @return itself
         */
        public Builder setYandexMoneyCards(List<YandexMoneyCard> yandexMoneyCards) {
            this.yandexMoneyCards = yandexMoneyCards;
            return this;
        }

        /**
         * @return {@link AccountInfo} instance
         */
        public AccountInfo createAccountInfo() {
            return new AccountInfo(this);
        }
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
