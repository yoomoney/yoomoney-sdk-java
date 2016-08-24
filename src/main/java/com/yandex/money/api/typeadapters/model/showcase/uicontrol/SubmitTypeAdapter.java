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

package com.yandex.money.api.typeadapters.model.showcase.uicontrol;

import com.yandex.money.api.model.showcase.components.uicontrols.Submit;

/**
 * Type adapter for {@link Submit} component.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class SubmitTypeAdapter extends ControlTypeAdapter<Submit, Submit.Builder> {

    private static final SubmitTypeAdapter INSTANCE = new SubmitTypeAdapter();

    private SubmitTypeAdapter() {
    }

    /**
     * @return instance of this class
     */
    public static SubmitTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    protected Submit createInstance(Submit.Builder builder) {
        return builder.create();
    }

    @Override
    protected Submit.Builder createBuilderInstance() {
        return new Submit.Builder();
    }

    @Override
    protected Class<Submit> getType() {
        return Submit.class;
    }
}
