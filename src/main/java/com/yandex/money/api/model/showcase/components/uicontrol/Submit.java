package com.yandex.money.api.model.showcase.components.uicontrol;

import com.yandex.money.api.utils.ToStringBuilder;

/**
 * Submit button.
 *
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public final class Submit extends Control {

    private Submit(Builder builder) {
        super(builder);
    }

    @Override
    protected ToStringBuilder getToStringBuilder() {
        return super.getToStringBuilder().setName("Submit");
    }

    /**
     * {@link Submit} builder.
     */
    public static final class Builder extends Control.Builder {

        @Override
        public Submit create() {
            return new Submit(this);
        }
    }
}
