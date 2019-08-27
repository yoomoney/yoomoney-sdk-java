package com.yandex.money.api.typeadapters.model.showcase.uicontrol;

import com.yandex.money.api.model.showcase.components.uicontrols.AdditionalData;

public class AdditionalDataTypeAdapter extends ParameterControlTypeAdapter<AdditionalData, AdditionalData.Builder> {

    private static final AdditionalDataTypeAdapter INSTANCE = new AdditionalDataTypeAdapter();

    private AdditionalDataTypeAdapter() { }

    public static AdditionalDataTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    protected AdditionalData.Builder createBuilderInstance() {
        return new AdditionalData.Builder();
    }

    @Override
    protected AdditionalData createInstance(AdditionalData.Builder builder) {
        return builder.create();
    }

    @Override
    protected Class<AdditionalData> getType() {
        return AdditionalData.class;
    }
}
