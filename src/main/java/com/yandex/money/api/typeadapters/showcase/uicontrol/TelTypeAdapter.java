package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.yandex.money.api.model.showcase.components.uicontrol.Tel;

import java.lang.reflect.Type;

/**
 * Type adapter for {@link Tel} component.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class TelTypeAdapter extends ParameterControlTypeAdapter<Tel, Tel.Builder> {

    public static final TelTypeAdapter INSTANCE = new TelTypeAdapter();

    private TelTypeAdapter() {
    }

    @Override
    protected Tel.Builder createBuilderInstance() {
        return new Tel.Builder();
    }

    @Override
    protected Type getType() {
        return Tel.class;
    }

    @Override
    protected Tel createInstance(Tel.Builder builder) {
        return builder.create();
    }
}
