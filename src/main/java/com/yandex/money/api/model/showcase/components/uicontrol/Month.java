package com.yandex.money.api.model.showcase.components.uicontrol;

import com.yandex.money.api.utils.ToStringBuilder;

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

    @Override
    public DateTimeFormatter getFormatter() {
        return FORMATTER;
    }

    @Override
    protected ToStringBuilder getToStringBuilder() {
        return super.getToStringBuilder().setName("Month");
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