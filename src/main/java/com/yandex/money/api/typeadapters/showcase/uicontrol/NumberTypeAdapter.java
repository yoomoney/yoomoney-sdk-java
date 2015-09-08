package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.yandex.money.api.model.showcase.components.uicontrol.Number;

/**
 * Type adapter for {@link Number} component.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class NumberTypeAdapter extends BaseNumberTypeAdapter<Number, Number.Builder> {

    private static final NumberTypeAdapter INSTANCE = new NumberTypeAdapter();

    private NumberTypeAdapter() {
    }

    /**
     * @return instance of this class
     */
    public static NumberTypeAdapter getInstance() {
        return INSTANCE;
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
