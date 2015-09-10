package com.yandex.money.api.model.showcase;

import com.yandex.money.api.model.AllowedMoneySource;
import com.yandex.money.api.model.showcase.components.Component;
import com.yandex.money.api.model.showcase.components.Parameter;
import com.yandex.money.api.model.showcase.components.container.Group;
import com.yandex.money.api.model.showcase.components.uicontrol.Select;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public final class Showcase {

    public final String title;
    public final Map<String, String> hiddenFields;
    public final Group form;
    public final List<AllowedMoneySource> moneySources;
    public final List<Error> errors;

    private Showcase(Builder builder) {

        if (builder.title == null) {
            throw new NullPointerException("title is null");
        }
        if (builder.hiddenFields == null) {
            throw new NullPointerException("hiddenFields is null");
        }
        if (builder.form == null) {
            throw new NullPointerException("form is null");
        }
        if (builder.moneySources == null) {
            throw new NullPointerException("moneySources is null");
        }
        if (builder.errors == null) {
            throw new NullPointerException("errors is null");
        }
        this.title = builder.title;
        this.hiddenFields = Collections.unmodifiableMap(builder.hiddenFields);
        this.form = builder.form;
        this.moneySources = Collections.unmodifiableList(builder.moneySources);
        this.errors = Collections.unmodifiableList(builder.errors);
    }

    /**
     * See Showcases class.
     *
     * @return key-value pairs of payment parameters
     */
    public Map<String, String> getPaymentParameters() {
        Map<String, String> params = new HashMap<>();
        params.putAll(hiddenFields);
        fillPaymentParameters(params, form);
        return params;
    }

    private void fillPaymentParameters(Map<String, String> parameters, Group group) {
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
        private Map<String, String> hiddenFields;
        private Group form;
        private List<AllowedMoneySource> moneySources;
        private List<Error> errors;

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setHiddenFields(Map<String, String> hiddenFields) {
            this.hiddenFields = hiddenFields;
            return this;
        }

        public Builder setForm(Group form) {
            this.form = form;
            return this;
        }

        public Builder setMoneySources(List<AllowedMoneySource> moneySources) {
            this.moneySources = moneySources;
            return this;
        }

        public Builder setErrors(List<Error> errors) {
            this.errors = errors;
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
            this.alert = alert;
        }
    }
}
