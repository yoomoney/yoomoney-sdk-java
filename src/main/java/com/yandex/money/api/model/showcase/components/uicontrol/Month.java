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