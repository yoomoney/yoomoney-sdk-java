package com.yandex.money.api.model.showcase.components;

public final class Undefined extends Component {

    private static final Undefined INSTANCE = new Undefined();

    private Undefined() {
    }

    public static Component getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
