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

import com.yandex.money.api.model.showcase.components.uicontrols.Text;

/**
 * Type adapter for {@link Text} component.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class TextTypeAdapter extends BaseTextTypeAdapter<Text, Text.Builder> {

    private static final TextTypeAdapter INSTANCE = new TextTypeAdapter();

    private TextTypeAdapter() {
    }

    /**
     * @return instance of this class
     */
    public static TextTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    protected Class<Text> getType() {
        return Text.class;
    }

    @Override
    protected Text.Builder createBuilderInstance() {
        return new Text.Builder();
    }

    @Override
    protected Text createInstance(Text.Builder builder) {
        return builder.create();
    }
}
