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

import com.yandex.money.api.model.Error;
import com.yandex.money.api.model.Scope;
import com.yandex.money.api.net.FirstApiRequest;
import com.yandex.money.api.net.providers.HostsProvider;
import com.yandex.money.api.typeadapters.methods.AuxTokenTypeAdapter;

import java.util.Set;

/**
 * Auxiliary token. Has scopes which are subset of {@link com.yandex.money.api.methods.Token}'s
 * scopes.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class AuxToken {

    /**
     * auxiliary token
     */
    public final String auxToken;

    /**
     * error code
     */
    public final Error error;

    public AuxToken(String auxToken, Error error) {
        this.auxToken = auxToken;
        this.error = error;
    }

    @Override
    public String toString() {
        return "AuxToken{" +
                "auxToken='" + auxToken + '\'' +
                ", error=" + error +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AuxToken auxToken1 = (AuxToken) o;

        return !(auxToken != null ? !auxToken.equals(auxToken1.auxToken)
                : auxToken1.auxToken != null) && error == auxToken1.error;
    }

    @Override
    public int hashCode() {
        int result = auxToken != null ? auxToken.hashCode() : 0;
        result = 31 * result + (error != null ? error.hashCode() : 0);
        return result;
    }

    /**
     * Requests for an auxiliary token.
     * <p/>
     * Authorized session required.
     */
    public static final class Request extends FirstApiRequest<AuxToken> {

        public Request(Set<Scope> scopes) {
            super(AuxTokenTypeAdapter.getInstance());
            addParameter("scope", Scope.createScopeParameter(scopes));
        }

        @Override
        protected String requestUrlBase(HostsProvider hostsProvider) {
            return hostsProvider.getMoneyApi() + "/token-aux";
        }
    }
}
