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

package com.yandex.money.api.model.showcase.components;

import static com.yandex.money.api.util.Common.checkNotNull;

/**
 * Block of plain text.
 *
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public class TextBlock {

    /**
     * Text.
     */
    public final String text;

    public TextBlock(String text) {
        this.text = checkNotNull(text, "text");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextBlock textBlock = (TextBlock) o;

        return text.equals(textBlock.text);
    }

    @Override
    public int hashCode() {
        return text.hashCode();
    }

    /**
     * Link.
     */
    public static class WithLink extends TextBlock {

        public final String link;

        public WithLink(String label, String link) {
            super(label);
            this.link = checkNotNull(link, "link");
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            WithLink withLink = (WithLink) o;

            return link.equals(withLink.link);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + link.hashCode();
            return result;
        }
    }
}
