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

package com.yandex.money.api.model.showcase;

import com.yandex.money.api.model.AllowedMoneySource;
import com.yandex.money.api.model.showcase.components.Component;
import com.yandex.money.api.model.showcase.components.Parameter;
import com.yandex.money.api.model.showcase.components.containers.Group;
import com.yandex.money.api.model.showcase.components.uicontrols.Select;
import com.yandex.money.api.net.BaseApiRequest;
import com.yandex.money.api.net.providers.HostsProvider;
import com.yandex.money.api.typeadapters.showcase.ShowcaseTypeAdapter;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.yandex.money.api.utils.Common.checkNotNull;

/**
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public final class Showcase {

    public final String title;
    public final Map<String, String> hiddenFields;
    public final Group form;
    public final Set<AllowedMoneySource> moneySources;
    public final List<Error> errors;

    private Showcase(Builder builder) {
        title = checkNotNull(builder.title, "title");
        form = builder.form;
        hiddenFields = Collections.unmodifiableMap(builder.hiddenFields);
        moneySources = Collections.unmodifiableSet(builder.moneySources);
        errors = Collections.unmodifiableList(builder.errors);
    }

    /**
     * @return key-value pairs of payment parameters
     */
    public Map<String, String> getPaymentParameters() {
        Map<String, String> params = new HashMap<>();
        params.putAll(hiddenFields);
        fillPaymentParameters(params, form);
        return params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Showcase showcase = (Showcase) o;

        if (!title.equals(showcase.title)) return false;
        if (!hiddenFields.equals(showcase.hiddenFields)) return false;
        if (form != null ? !form.equals(showcase.form) : showcase.form != null) return false;
        if (!moneySources.equals(showcase.moneySources)) return false;
        return errors.equals(showcase.errors);
    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + hiddenFields.hashCode();
        result = 31 * result + (form != null ? form.hashCode() : 0);
        result = 31 * result + moneySources.hashCode();
        result = 31 * result + errors.hashCode();
        return result;
    }

    private static void fillPaymentParameters(Map<String, String> parameters, Group group) {
        for (Component component : group.items) {
            if (component instanceof Group) {
                fillPaymentParameters(parameters, (Group) component);
            } else if (component instanceof Parameter) {
                Parameter parameter = (Parameter) component;
                parameters.put(parameter.getName(), parameter.getValue());
                if (component instanceof Select) {
                    Group selectedGroup = ((Select) component).getSelectedOption().group;
                    if (selectedGroup != null) {
                        fillPaymentParameters(parameters, selectedGroup);
                    }
                }
            }
        }
    }

    public static class Builder {

        private String title;
        private Map<String, String> hiddenFields = Collections.emptyMap();
        private Group form;
        private Set<AllowedMoneySource> moneySources = Collections.emptySet();
        private List<Error> errors = Collections.emptyList();

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setHiddenFields(Map<String, String> hiddenFields) {
            this.hiddenFields = checkNotNull(hiddenFields, "hiddenFields");
            return this;
        }

        public Builder setForm(Group form) {
            this.form = form;
            return this;
        }

        public Builder setMoneySources(Set<AllowedMoneySource> moneySources) {
            this.moneySources = checkNotNull(moneySources, "moneySources");
            return this;
        }

        public Builder setErrors(List<Error> errors) {
            this.errors = checkNotNull(errors, "errors");
            return this;
        }

        public Showcase create() {
            return new Showcase(this);
        }
    }

    public static class Error {

        public final String name;
        public final String alert;

        public Error(String name, String alert) {
            this.name = name;
            this.alert = checkNotNull(alert, "alert");
        }
    }

    /**
     * Requests showcase.
     */
    public static final class Request extends BaseApiRequest<Showcase> {

        private final String patternId;
        private final String url;

        /**
         * Constructor.
         *
         * @param patternId payment pattern ID.
         */
        public Request(String patternId) {
            this(patternId, null, null);
        }

        /**
         * Constructor
         *
         * @param scid showcase identifier.
         */
        public Request(long scid) {
            this(String.valueOf(scid), null, null);
        }

        /**
         * Constructor.
         *
         * @param url    url
         * @param params post params
         */
        public Request(String url, Map<String, String> params) {
            this(null, url, params);
        }

        private Request(String patternId, String url, Map<String, String> params) {
            super(ShowcaseTypeAdapter.getInstance());
            if (url != null) {
                addParameters(checkNotNull(params, "params"));
            }
            this.patternId = patternId;
            this.url = url;
        }

        @Override
        public Method getMethod() {
            return url == null ? Method.GET : Method.POST;
        }

        @Override
        protected String requestUrlBase(HostsProvider hostsProvider) {
            return url == null ? hostsProvider.getMoneyApi() + "/showcase/" + patternId : url;
        }
    }
}
