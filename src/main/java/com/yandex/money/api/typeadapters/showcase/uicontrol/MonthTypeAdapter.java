package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.yandex.money.api.model.showcase.components.uicontrol.Month;

import org.joda.time.format.DateTimeFormatter;

/**
 * Type adapter for {@link Month} component.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class MonthTypeAdapter extends DateTypeAdapter<Month, Month.Builder> {

    public static final MonthTypeAdapter INSTANCE = new MonthTypeAdapter();

    private MonthTypeAdapter() {
    }

    @Override
    protected Month createInstance(Month.Builder builder) {
        return builder.create();
    }

    @Override
    protected Month.Builder createBuilderInstance() {
        return new Month.Builder();
    }

    @Override
    protected DateTimeFormatter getFormatter() {
        return Month.FORMATTER;
    }
}
