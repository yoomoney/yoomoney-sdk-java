package com.yandex.money.api.model.showcase.components.uicontrol;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

/**
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public final class Month extends Date {

    public static final String PATTERN = "yyyy-MM";
    public static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern(PATTERN)
            .withLocale(Locale.ENGLISH);

    private Month(Builder builder) {
        super(builder);
    }

    @Override
    protected DateTimeFormatter getFormatter() {
        return FORMATTER;
    }

    public static final class Builder extends Date.Builder {

        @Override
        public Month create() {
            return new Month(this);
        }
    }
}