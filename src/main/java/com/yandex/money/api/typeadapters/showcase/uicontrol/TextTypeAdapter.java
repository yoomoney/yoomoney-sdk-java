package com.yandex.money.api.typeadapters.showcase.uicontrol;

import com.yandex.money.api.model.showcase.components.uicontrol.Text;

/**
 * Type adapter for {@link Text} component.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class TextTypeAdapter extends BaseTextTypeAdapter<Text, Text.Builder> {

    private static final TextTypeAdapter INSTANCE = new TextTypeAdapter();

    private TextTypeAdapter() {
    }

    /**
     * @return instance of this class
     */
    public static TextTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    protected Class<Text> getType() {
        return Text.class;
    }

    @Override
    protected Text.Builder createBuilderInstance() {
        return new Text.Builder();
    }

    @Override
    protected Text createInstance(Text.Builder builder) {
        return builder.create();
    }
}
