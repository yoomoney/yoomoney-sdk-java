package com.yandex.money.api.model.showcase.components.uicontrol;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

/**
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public class Date extends ParameterControl {

    public static final String PATTERN = "yyyy-MM-dd";
    public static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern(PATTERN)
            .withLocale(Locale.ENGLISH);

    public final DateTime min;
    public final DateTime max;

    protected Date(Builder builder) {
        super(builder);
        if (builder.min != null && builder.max != null && builder.min.isAfter(builder.max)) {
            throw new IllegalArgumentException("min > max");
        }
        min = builder.min;
        max = builder.max;
    }

    public static DateTime parseDate(String date, DateTimeFormatter formatter) {
        if (date == null || date.isEmpty()) {
            return null;
        }
        if (formatter == null) {
            throw new NullPointerException("pattern is null");
        }
        return DateTime.parse(date, formatter);
    }

    @Override
    public boolean isValid(String value) {
        return super.isValid(value) && isValidInner(value);
    }

    public DateTime toDateTime() {
        return parseDate(getValue(), getFormatter());
    }

    protected DateTimeFormatter getFormatter() {
        return FORMATTER;
    }

    private boolean isValidInner(String value) {
        try {
            DateTime dateTime = parseDate(value, getFormatter());
            return dateTime == null ||
                    (min == null || min.isBefore(dateTime) || min.isEqual(dateTime)) &&
                            (max == null || max.isAfter(dateTime) || max.isEqual(dateTime));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static class Builder extends ParameterControl.Builder {

        private DateTime min;
        private DateTime max;

        @Override
        public Date create() {
            return new Date(this);
        }

        public Builder setMin(DateTime min) {
            this.min = min;
            return this;
        }

        public Builder setMax(DateTime max) {
            this.max = max;
            return this;
        }
    }
}
