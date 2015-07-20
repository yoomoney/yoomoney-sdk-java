package com.yandex.money.api.model.showcase.components.uicontrol;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

/**
 * The month and year control.
 *
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public final class Month extends Date {

    /**
     * Acceptable pattern.
     */
    public static final String PATTERN = "yyyy-MM";

    /**
     * (De)serialization formatter.
     */
    public static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern(PATTERN)
            .withLocale(Locale.ENGLISH);

    private Month(Builder builder) {
        super(builder);
    }

    /**
     * Getter.
     * <p/>
     * TODO: delete this?
     *
     * @return returns {@link Month} formatter.
     */
    @Override
    protected DateTimeFormatter getFormatter() {
        return FORMATTER;
    }

    /**
     * {@link Month} builder.
     */
    public static final class Builder extends Date.Builder {

        @Override
        public Month create() {
            return new Month(this);
        }
    }
}