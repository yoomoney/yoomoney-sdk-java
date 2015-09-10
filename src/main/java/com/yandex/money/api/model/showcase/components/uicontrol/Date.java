/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 NBCO Yandex.Money LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.yandex.money.api.model.showcase.components.uicontrol;

import com.yandex.money.api.utils.ToStringBuilder;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

/**
 * Date control specified by {@link Date#PATTERN} (for example 2015-05-30).
 *
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public class Date extends ParameterControl {

    /**
     * Date format.
     */
    public static final String PATTERN = "yyyy-MM-dd";

    /**
     * Date formatter.
     * <p/>
     * TODO: refactor this field or {@link Date#getFormatter()}.
     */
    public static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern(PATTERN)
            .withLocale(Locale.ENGLISH);

    /**
     * Minimal allowed value.
     */
    public final DateTime min;

    /**
     * Maximal allowed value.
     */
    public final DateTime max;

    protected Date(Builder builder) {
        super(builder);
        if (builder.min != null && builder.max != null && builder.min.isAfter(builder.max)) {
            throw new IllegalArgumentException("min > max");
        }
        min = builder.min;
        max = builder.max;
    }

    /**
     * Parses a {@link DateTime} from the specified string using a formatter.
     *
     * @param date      value to parse.
     * @param formatter {@link DateTimeFormatter}.
     */
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

    /**
     * Parses internal value to {@link DateTime}.
     */
    public DateTime toDateTime() {
        return parseDate(getValue(), getFormatter());
    }

    /**
     * @return control's formatter.
     */
    public DateTimeFormatter getFormatter() {
        return FORMATTER;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Date date = (Date) o;

        return !(min != null ? !min.equals(date.min) : date.min != null) &&
                !(max != null ? !max.equals(date.max) : date.max != null);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (min != null ? min.hashCode() : 0);
        result = 31 * result + (max != null ? max.hashCode() : 0);
        return result;
    }

    @Override
    protected ToStringBuilder getToStringBuilder() {
        return super.getToStringBuilder()
                .setName("Date")
                .append("min", min)
                .append("max", max);
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

    /**
     * {@link Date} builder.
     */
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
