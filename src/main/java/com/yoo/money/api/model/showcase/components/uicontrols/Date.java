/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 NBCO YooMoney LLC
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

package com.yoo.money.api.model.showcase.components.uicontrols;

import com.yoo.money.api.time.DateTime;
import com.yoo.money.api.time.Period;
import com.yoo.money.api.util.Strings;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Date control specified by {@link Date#PATTERN} (for example 2015-05-30).
 *
 * @author Aleksandr Ershov (support@yoomoney.ru)
 */
public class Date extends ParameterControl {

    /**
     * Date format.
     */
    public static final String PATTERN = "yyyy-MM-dd";

    /**
     * Date formatter.
     */
    public static final DateFormat FORMATTER = new SimpleDateFormat(PATTERN, Locale.ENGLISH);

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
     * @param formatter {@link DateFormat}.
     */
    public static DateTime parseDate(String date, DateFormat formatter) throws ParseException {
        if (Strings.isNullOrEmpty(date)) {
            return null;
        }
        String[] split = date.split("/");
        if (split.length > 1) {
            if (split[0].startsWith("P")) {
                return parseWithPeriod(split[1], split[0], false, formatter);
            } else if (split[1].startsWith("P")) {
                return parseWithPeriod(split[0], split[1], true, formatter);
            } else {
                throw new IllegalArgumentException("unable to parse date: " + date);
            }
        } else {
            return parse(date, formatter);
        }
    }

    @Override
    public boolean isValid(String value) {
        return super.isValid(value) && isValidInner(value);
    }

    /**
     * Parses internal value to {@link DateTime}.
     */
    public DateTime toDateTime() {
        try {
            return parseDate(getValue(), getFormatter());
        } catch (ParseException e) {
            throw new IllegalStateException("illegal value: " + getValue());
        }
    }

    /**
     * @return control's formatter.
     */
    public DateFormat getFormatter() {
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

    private static DateTime parse(String dateTime, DateFormat formatter) throws ParseException {
        java.util.Date parsed = dateTime.equals("now") ?
                formatter.parse(formatter.format(DateTime.now().getDate())) :
                formatter.parse(dateTime);
        return DateTime.from(parsed);
    }

    private static DateTime parseWithPeriod(String dateTime, String period, boolean add, DateFormat formatter)
            throws ParseException {
        return parseWithPeriod(parse(dateTime, formatter), Period.parse(period), add);
    }

    private static DateTime parseWithPeriod(DateTime dateTime, Period period, boolean add) {
        return add ? dateTime.plus(period) : dateTime.minus(period);
    }

    private boolean isValidInner(String value) {
        try {
            DateTime dateTime = parseDate(value, getFormatter());
            return dateTime == null ||
                    (min == null || min.isBefore(dateTime) || min.equals(dateTime)) &&
                            (max == null || max.isAfter(dateTime) || max.equals(dateTime));
        } catch (IllegalArgumentException | ParseException e) {
            return false;
        }
    }

    /**
     * {@link Date} builder.
     */
    public static class Builder extends ParameterControl.Builder {

        DateTime min;
        DateTime max;

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
