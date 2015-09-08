package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.yandex.money.api.model.showcase.components.uicontrol.Submit;

/**
 * Type adapter for {@link Submit} component.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class SubmitTypeAdapter extends ControlTypeAdapter<Submit, Submit.Builder> {

    private static final SubmitTypeAdapter INSTANCE = new SubmitTypeAdapter();

    private SubmitTypeAdapter() {
    }

    /**
     * @return instance of this class
     */
    public static SubmitTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    protected Submit createInstance(Submit.Builder builder) {
        return builder.create();
    }

    @Override
    protected Submit.Builder createBuilderInstance() {
        return new Submit.Builder();
    }

    @Override
    protected Class<Submit> getType() {
        return Submit.class;
    }
}
