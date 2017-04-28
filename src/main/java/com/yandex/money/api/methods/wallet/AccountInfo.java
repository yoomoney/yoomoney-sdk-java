/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 NBCO Yandex.Money LLC
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

package com.yandex.money.api.methods.wallet;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.yandex.money.api.model.AccountStatus;
import com.yandex.money.api.model.AccountType;
import com.yandex.money.api.model.BalanceDetails;
import com.yandex.money.api.model.Currency;
import com.yandex.money.api.net.FirstApiRequest;
import com.yandex.money.api.net.providers.HostsProvider;
import com.yandex.money.api.typeadapters.model.NumericCurrencyTypeAdapter;

import java.math.BigDecimal;

import static com.yandex.money.api.util.Common.checkNotEmpty;
import static com.yandex.money.api.util.Common.checkNotNull;

/**
 * Information of user account.
 *
 * @author Roman Tsirulnikov (romanvt@yamoney.ru)
 */
public class AccountInfo {

    /**
     * account number
     */
    @SerializedName("account")
    public final String account;

    /**
     * current balance
     */
    @SerializedName("balance")
    public final BigDecimal balance;

    /**
     * account's currency
     */
    @SerializedName("currency")
    @JsonAdapter(NumericCurrencyTypeAdapter.class)
    public final Currency currency;

    /**
     * account's status
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("account_status")
    public final AccountStatus accountStatus;

    /**
     * account's type
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("account_type")
    public final AccountType accountType;

    /**
     * balance details
     */
    @SuppressWarnings("WeakerAccess")
    @SerializedName("balance_details")
    public final BalanceDetails balanceDetails;

    @SuppressWarnings("WeakerAccess")
    protected AccountInfo(Builder builder) {
        account = checkNotEmpty(builder.account, "account");
        balance = checkNotNull(builder.balance, "balance");
        currency = checkNotNull(builder.currency, "currency");
        accountStatus = checkNotNull(builder.accountStatus, "accountStatus");
        accountType = checkNotNull(builder.accountType, "accountType");
        balanceDetails = checkNotNull(builder.balanceDetails, "balanceDetails");
    }

    protected AccountInfo() {
        account = null;
        balance = BigDecimal.ZERO;
        currency = Currency.RUB;
        accountStatus = AccountStatus.ANONYMOUS;
        accountType = AccountType.PERSONAL;
        balanceDetails = BalanceDetails.ZERO;
    }

    @Override
    public String toString() {
        return "AccountInfo{" +
                "account='" + account + '\'' +
                ", balance=" + balance +
                ", currency=" + currency +
                ", accountStatus=" + accountStatus +
                ", accountType=" + accountType +
                ", balanceDetails=" + balanceDetails +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountInfo that = (AccountInfo) o;

        return account.equals(that.account) && balance.equals(that.balance) && currency == that.currency &&
                accountStatus == that.accountStatus && accountType == that.accountType &&
                balanceDetails.equals(that.balanceDetails);
    }

    @Override
    public int hashCode() {
        int result = account.hashCode();
        result = 31 * result + balance.hashCode();
        result = 31 * result + currency.hashCode();
        result = 31 * result + accountStatus.hashCode();
        result = 31 * result + accountType.hashCode();
        result = 31 * result + balanceDetails.hashCode();
        return result;
    }

    /**
     * Creates {@link AccountInfo} instance.
     */
    public static class Builder {

        String account;
        BigDecimal balance = BigDecimal.ZERO;
        Currency currency = Currency.RUB;
        AccountStatus accountStatus = AccountStatus.ANONYMOUS;
        AccountType accountType = AccountType.PERSONAL;
        BalanceDetails balanceDetails = BalanceDetails.ZERO;

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
         * @param balanceDetails balance details
         * @return itself
         */
        public Builder setBalanceDetails(BalanceDetails balanceDetails) {
            this.balanceDetails = balanceDetails;
            return this;
        }

        /**
         * @return {@link AccountInfo} instance
         */
        public AccountInfo create() {
            return new AccountInfo(this);
        }
    }

    /**
     * Requests for {@link AccountInfo}.
     * <p/>
     * Authorized session required.
     */
    public static final class Request extends FirstApiRequest<AccountInfo> {

        public Request() {
            super(AccountInfo.class);
        }

        @Override
        protected String requestUrlBase(HostsProvider hostsProvider) {
            return hostsProvider.getMoneyApi() + "/account-info";
        }
    }
}
