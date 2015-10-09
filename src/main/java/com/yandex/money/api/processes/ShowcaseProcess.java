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

package com.yandex.money.api.processes;

import com.yandex.money.api.exceptions.ResourceNotFoundException;
import com.yandex.money.api.net.DocumentProvider;
import com.yandex.money.api.net.ShowcaseContext;

import java.io.IOException;

/**
 * This class connects {@link DocumentProvider} and {@link ShowcaseContext} class and provides
 * convenient methods to work with them.
 *
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class ShowcaseProcess implements Process {

    /**
     * Related context which encapsulates current step and overall state
     */
    public final ShowcaseContext showcaseContext;
    private final DocumentProvider documentProvider;

    public ShowcaseProcess(DocumentProvider documentProvider, ShowcaseContext showcaseContext) {
        if (documentProvider == null) {
            throw new NullPointerException("document provider is null");
        }
        if (showcaseContext == null) {
            throw new NullPointerException("showcaseContext is null");
        }
        this.documentProvider = documentProvider;
        this.showcaseContext = showcaseContext;
    }

    /**
     * Moves (submits) showcase context to the next step.
     *
     * @return {@code true} in case of completed process and {@code false} otherwise
     * @throws IOException               connection timeout/other IO stuff
     * @throws ResourceNotFoundException wrong URL
     */
    @Override
    public boolean proceed() throws IOException, ResourceNotFoundException {
        if (isCompleted()) {
            return true;
        }
        documentProvider.submitShowcase(showcaseContext);
        return isCompleted();
    }

    /**
     * Moves (submits) showcase context to the next step.
     *
     * @return {@code true} in case of already completed process and {@code false} otherwise
     */
    @Override
    public boolean repeat() throws IOException, ResourceNotFoundException {
        return proceed();
    }

    /**
     * Steps back to previous state.
     */
    public void back() {
        showcaseContext.popStep();
    }

    private boolean isCompleted() {
        return showcaseContext.getState() == ShowcaseContext.State.COMPLETED;
    }
}
