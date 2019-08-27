package com.yandex.money.api.model.showcase.components.uicontrols;

public class AdditionalData extends ParameterControl {

    protected AdditionalData(Builder builder) {
        super(builder);
    }

    public static class Builder extends ParameterControl.Builder {

        @Override
        public AdditionalData create() {
            return new AdditionalData(this);
        }
    }
}
