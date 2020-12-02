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

package com.yoo.money.api.typeadapters.model.showcase.uicontrol;

import com.yoo.money.api.model.showcase.components.uicontrols.Email;

/**
 * Type adapter for {@link Email} component.
 *
 * @author Anton Ermak (support@yoomoney.ru)
 */
public final class EmailTypeAdapter extends ParameterControlTypeAdapter<Email, Email.Builder> {

    private static final EmailTypeAdapter INSTANCE = new EmailTypeAdapter();

    private EmailTypeAdapter() {
    }

    /**
     * @return instance of this class
     */
    public static EmailTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    protected Email.Builder createBuilderInstance() {
        return new Email.Builder();
    }

    @Override
    protected Email createInstance(Email.Builder builder) {
        return builder.create();
    }

    @Override
    public Class<Email> getType() {
        return Email.class;
    }
}
