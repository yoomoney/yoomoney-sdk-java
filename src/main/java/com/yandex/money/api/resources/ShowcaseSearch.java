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

package com.yandex.money.api.resources;

import com.yandex.money.api.model.Error;
import com.yandex.money.api.model.showcase.ShowcaseReference;
import com.yandex.money.api.net.GetRequest;
import com.yandex.money.api.net.HostsProvider;
import com.yandex.money.api.typeadapters.showcase.ShowcaseSearchTypeAdapter;
import com.yandex.money.api.utils.Strings;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class ShowcaseSearch {

    public final Error error;
    public final List<ShowcaseReference> result;
    public final String nextPage;

    public ShowcaseSearch(Error error, List<ShowcaseReference> result, String nextPage) {
        if (result == null) {
            throw new NullPointerException("result is null");
        }
        this.error = error;
        this.result = result;
        this.nextPage = nextPage;
    }

    public ShowcaseSearch(Error error) {
        this(error, new ArrayList<ShowcaseReference>(0), null);
    }

    public ShowcaseSearch(List<ShowcaseReference> result, String nextPage) {
        this(null, result, nextPage);
    }

    @Override
    public String toString() {
        return "ShowcaseSearch{" + "error=" + error + ", result=" + result + ", nextPage='" +
                nextPage + '\'' + '}';
    }

    public static final class Request extends GetRequest<ShowcaseSearch> {
        public Request(String query, int records) {
            this(query, records, null);
        }

        public Request(String query, int records, String uid) {
            super(ShowcaseSearch.class, ShowcaseSearchTypeAdapter.getInstance());
            if (Strings.isNullOrEmpty(query)) {
                throw new IllegalArgumentException("query is null or empty");
            }
            addParameter("query", query);
            addParameter("records", records);
            addParameter("uid", uid);
        }

        @Override
        public String requestUrl(HostsProvider hostsProvider) {
            return hostsProvider.getMoneyApi() + "/showcase-search";
        }
    }
}
