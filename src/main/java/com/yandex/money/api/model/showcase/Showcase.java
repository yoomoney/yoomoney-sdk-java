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
import java.util.Set;

/**
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public final class Showcase {

    public final String title;
    public final Map<String, String> hiddenFields;
    public final Group form;
    public final Set<AllowedMoneySource> moneySources;
    public final List<Error> errors;

    public Showcase(String title, Map<String, String> hiddenFields, Group form,
                    Set<AllowedMoneySource> moneySources, List<Error> errors) {

        if (title == null) {
            throw new NullPointerException("title is null");
        }
        if (hiddenFields == null) {
            throw new NullPointerException("hiddenFields is null");
        }
        if (form == null) {
            throw new NullPointerException("form is null");
        }
        if (moneySources == null) {
            throw new NullPointerException("moneySources is null");
        }
        if (errors == null) {
            throw new NullPointerException("errors is null");
        }
        this.title = title;
        this.hiddenFields = Collections.unmodifiableMap(hiddenFields);
        this.form = form;
        this.moneySources = Collections.unmodifiableSet(moneySources);
        this.errors = Collections.unmodifiableList(errors);
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

    public static class Error {

        public final String name;
        public final String alert;

        public Error(String name, String alert) {
            this.name = name;
            this.alert = alert;
        }
    }
}
