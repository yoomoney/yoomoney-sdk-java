package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.yandex.money.api.model.showcase.components.uicontrol.TextArea;

/**
 * Type adapter for {@link TextArea} component.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class TextAreaTypeAdapter extends BaseTextAreaTypeAdapter<TextArea, TextArea.Builder> {

    private static final TextAreaTypeAdapter INSTANCE = new TextAreaTypeAdapter();

    /**
     * @return instance of this class
     */
    public static TextAreaTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    protected TextArea.Builder createBuilderInstance() {
        return new TextArea.Builder();
    }

    @Override
    protected Class<TextArea> getType() {
        return TextArea.class;
    }

    @Override
    protected TextArea createInstance(TextArea.Builder builder) {
        return builder.create();
    }
}
