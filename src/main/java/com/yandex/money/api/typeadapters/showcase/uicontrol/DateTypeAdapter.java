package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.yandex.money.api.model.showcase.components.uicontrol.Date;


/**
 * Base type adapter for subclasses of {@link Date} component.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class DateTypeAdapter extends BaseDateTypeAdapter<Date, Date.Builder> {

    public static final DateTypeAdapter INSTANCE = new DateTypeAdapter();

    private DateTypeAdapter() {
    }

    @Override
    protected Date.Builder createBuilderInstance() {
        return new Date.Builder();
    }

    @Override
    protected Date createInstance(Date.Builder builder) {
        return builder.create();
    }

    @Override
    protected Class<Date> getType() {
        return Date.class;
    }
}
