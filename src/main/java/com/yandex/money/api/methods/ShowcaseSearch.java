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
import com.yandex.money.api.model.showcase.ShowcaseReference;
import com.yandex.money.api.net.ApiRequest;
import com.yandex.money.api.net.DocumentProvider;
import com.yandex.money.api.net.GetRequest;
import com.yandex.money.api.net.HostsProvider;
import com.yandex.money.api.typeadapters.showcase.ShowcaseSearchTypeAdapter;
import com.yandex.money.api.utils.Strings;

import java.util.Collections;
import java.util.List;

import static com.yandex.money.api.utils.Common.checkNotNull;

/**
 * This class wraps result of showcase searching provided by response of
 * {@link ShowcaseSearch.Request} call.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public final class ShowcaseSearch {

    /**
     * Error code. May be {@code null}.
     */
    public final Error error;

    /**
     * List of {@link ShowcaseReference}.
     */
    public final List<ShowcaseReference> result;

    /**
     * Next page marker.
     */
    public final String nextPage;

    private ShowcaseSearch(Error error, List<ShowcaseReference> result, String nextPage) {
        checkNotNull(result, "result");
        this.error = error;
        this.result = result;
        this.nextPage = nextPage;
    }

    /**
     * Constructs successful search call.
     *
     * @param result   obtained items
     * @param nextPage next page marker
     */
    public static ShowcaseSearch success(List<ShowcaseReference> result, String nextPage) {
        return new ShowcaseSearch(null, result, nextPage);
    }

    /**
     * Constructs failed instance.
     *
     * @param error reason
     */
    public static ShowcaseSearch failure(Error error) {
        checkNotNull(error, "error");
        return new ShowcaseSearch(error, Collections.<ShowcaseReference>emptyList(), null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShowcaseSearch that = (ShowcaseSearch) o;

        return error == that.error && result.equals(that.result) &&
                !(nextPage != null ? !nextPage.equals(that.nextPage) : that.nextPage != null);
    }

    @Override
    public int hashCode() {
        int result1 = error != null ? error.hashCode() : 0;
        result1 = 31 * result1 + result.hashCode();
        result1 = 31 * result1 + (nextPage != null ? nextPage.hashCode() : 0);
        return result1;
    }

    @Override
    public String toString() {
        return "ShowcaseSearch{" +
                "error=" + error +
                ", result=" + result +
                ", nextPage='" + nextPage + '\'' +
                '}';
    }

    /**
     * This class should be used for obtaining {@link ShowcaseReference} instances in
     * {@link DocumentProvider#fetch(ApiRequest)} call.
     */
    public static class Request extends GetRequest<ShowcaseSearch> {

        /**
         * Constructor.
         *
         * @param query   search terms
         * @param records number of records to requests from remote server
         */
        public Request(String query, int records) {
            super(ShowcaseSearch.class, ShowcaseSearchTypeAdapter.getInstance());
            if (Strings.isNullOrEmpty(query)) {
                throw new IllegalArgumentException("query is null or empty");
            }
            addParameter("query", query);
            addParameter("records", records);
        }

        @Override
        public String requestUrl(HostsProvider hostsProvider) {
            return hostsProvider.getMoneyApi() + "/showcase-search";
        }
    }
}
