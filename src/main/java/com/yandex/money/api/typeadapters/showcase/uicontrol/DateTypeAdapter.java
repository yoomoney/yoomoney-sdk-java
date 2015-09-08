package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.yandex.money.api.model.showcase.components.uicontrol.Date;

/**
 * Type adapter for {@link Date} component.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class DateTypeAdapter extends BaseDateTypeAdapter<Date, Date.Builder> {

    private static final DateTypeAdapter INSTANCE = new DateTypeAdapter();

    private DateTypeAdapter() {
    }

    /**
     * @return instance of this class
     */
    public static DateTypeAdapter getInstance() {
        return INSTANCE;
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
