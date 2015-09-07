package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.yandex.money.api.model.showcase.components.uicontrol.Number;

/**
 * Base type adapter for subclasses of {@link Number}.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class NumberTypeAdapter extends BaseNumberTypeAdapter<Number, Number.Builder> {

    public static final NumberTypeAdapter INSTANCE = new NumberTypeAdapter();

    private NumberTypeAdapter() {
    }

    @Override
    protected Number.Builder createBuilderInstance() {
        return new Number.Builder();
    }

    @Override
    protected Number createInstance(Number.Builder builder) {
        return builder.create();
    }

    @Override
    protected Class<Number> getType() {
        return Number.class;
    }
}
