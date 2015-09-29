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

package com.yandex.money.test.processes;

import com.squareup.okhttp.OkHttpClient;
import com.yandex.money.api.model.AllowedMoneySource;
import com.yandex.money.api.model.showcase.Showcase;
import com.yandex.money.api.model.showcase.components.containers.Group;
import com.yandex.money.api.model.showcase.components.uicontrols.Select;
import com.yandex.money.api.model.showcase.components.uicontrols.Text;
import com.yandex.money.api.net.ApiClient;
import com.yandex.money.api.net.ApiRequest;
import com.yandex.money.api.net.DocumentProvider;
import com.yandex.money.api.net.HostsProvider;
import com.yandex.money.api.net.ShowcaseContext;
import com.yandex.money.api.net.UserAgent;
import com.yandex.money.api.processes.ShowcaseProcess;
import com.yandex.money.api.typeadapters.showcase.ShowcaseTypeAdapter;
import com.yandex.money.api.utils.Language;
import com.yandex.money.test.Utils;

import org.joda.time.DateTime;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Stack;

/**
 * Bundle of tests for proving that ShowcaseProcess proceed* and back methods
 * work as expected. Note: 5551 showcase is used without "pattern" and "length" constrains.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class ShowcaseProcessTest extends Assert {

    private static final String BILLS_SECOND_STEP_URL = "https://money.yandex" +
            ".ru/api/showcase/validate/5551/step_INN_3038";
    private static final String SAMPLE_INN = "7805498776";

    /**
     * Tests that complex showcase successfully proceeds two steps asynchronously.
     *
     * @throws Exception
     */
    @Test
    public void testProceedSuccess() throws Exception {
        final ShowcaseProcess showcaseProcess = initShowcaseProcess();
        final ShowcaseContext showcaseContext = showcaseProcess.showcaseContext;

        Showcase showcase = showcaseContext
                .getCurrentStep()
                .showcase;
        final Text inn = (Text) showcase.form.items.get(0);
        inn.setValue(SAMPLE_INN);
        showcaseProcess.proceed();

        assertEquals(showcaseContext.getHistorySize(), 1);

        showcase = showcaseContext
                .getCurrentStep()
                .showcase;
        ((Text) showcase.form.items.get(0)).setValue("Содружество Авангард");
        ((Select) showcase.form.items.get(1)).setValue("40702810055240000859");

        showcaseProcess.proceed();
        assertEquals(showcaseContext.getHistorySize(), 2);

        showcaseProcess.proceed();
        assertEquals(showcaseContext.getHistorySize(), 3);
    }

    /**
     * Tests that erroneous leads to
     * {@link ShowcaseContext.State#INVALID_PARAMS} state.
     */
    @Test
    public void testProceedFail() throws Exception {
        Showcase showcase = loadFromResource();
        ShowcaseContext.Step currentStep = new ShowcaseContext.Step(showcase,
                BILLS_SECOND_STEP_URL);

        final ShowcaseContext showcaseContext = new ShowcaseContext(
                new Stack<ShowcaseContext.Step>(),
                new DateTime(),
                currentStep,
                Collections.<String, String>emptyMap(),
                ShowcaseContext.State.UNKNOWN);
        final ShowcaseProcess showcaseProcess = new ShowcaseProcess(getDocumentProvider(),
                showcaseContext);

        showcaseProcess.proceed();
        assertEquals(showcaseContext.getState(), ShowcaseContext.State.INVALID_PARAMS);
        assertEquals(showcaseContext.getHistorySize(), 0);
    }

    /**
     * Tests that recovery from erroneous input works as expected.
     */
    @Test
    public void testRepeat() throws Exception {
        Showcase showcase = loadFromResource();
        final ShowcaseContext.Step currentStep = new ShowcaseContext.Step(showcase,
                BILLS_SECOND_STEP_URL);

        final ShowcaseContext showcaseContext = new ShowcaseContext(
                new Stack<ShowcaseContext.Step>(),
                new DateTime(),
                currentStep,
                Collections.<String, String>emptyMap(),
                ShowcaseContext.State.UNKNOWN
        );
        final ShowcaseProcess showcaseProcess = new ShowcaseProcess(getDocumentProvider(),
                showcaseContext);

        Text inn = (Text) showcase.form.items.get(0);
        inn.setValue("erroneous input");
        showcaseProcess.proceed();
        assertEquals(showcaseContext.getHistorySize(), 0);

        // repeat proceed with allowed inn
        showcase = showcaseContext.getCurrentStep().showcase;
        inn = (Text) showcase.form.items.get(0);
        inn.setValue(SAMPLE_INN);
        showcaseProcess.proceed();
        assertEquals(showcaseContext.getHistorySize(), 1);

    }

    /**
     * Tests that showcase process returns {@code true} in case of completed showcase context.
     */
    @Test
    public void testComplete() throws Exception {
        final ShowcaseContext completedShowcaseContext = new ShowcaseContext(
                new Stack<ShowcaseContext.Step>(),
                new DateTime(),
                new ShowcaseContext.Step(getEmptyShowcase(), "http://foobar.foo"),
                Collections.<String, String>emptyMap(), ShowcaseContext.State.COMPLETED);
        final ShowcaseProcess showcaseProcess = new ShowcaseProcess(getDocumentProvider(),
                completedShowcaseContext);

        assertTrue(showcaseProcess.proceed());
    }

    /**
     * Tests that {@link ShowcaseProcess#back()} wouldn't throw exception at the init step
     */
    @Test
    public void testBack() throws Exception {
        final ShowcaseProcess showcaseProcess = initShowcaseProcess();
        showcaseProcess.back();
    }

    private static Showcase getEmptyShowcase() {
        Group.Builder builder = new Group.Builder();
        Group group = builder.create();

        return new Showcase.Builder().setTitle("foo")
                .setErrors(Collections.<Showcase.Error>emptyList())
                .setForm(group)
                .setHiddenFields(Collections.<String, String>emptyMap())
                .setMoneySources(Collections.<AllowedMoneySource>emptySet())
                .create();
    }

    private static Showcase loadFromResource() throws Exception {
        return ShowcaseTypeAdapter.getInstance().fromJson(
                Utils.loadResource("/showcase/showcase_bills_novalidation.json"));
    }

    private static DocumentProvider getDocumentProvider() {
        final ApiClient client = getClientByHost("http://money.yandex.ru");
        return new DocumentProvider(client);
    }

    private static ShowcaseProcess initShowcaseProcess() throws Exception {
        final ApiRequest<Showcase> resReq = new Showcase.Request(5551);
        final DocumentProvider documentProvider = getDocumentProvider();
        final ShowcaseContext showcaseContext = documentProvider.getShowcase(resReq);
        return new ShowcaseProcess(documentProvider, showcaseContext);
    }


    private static HostsProvider getHostsProviderByString(final String host) {
        return new HostsProvider(true) {
            @Override
            public String getMoney() {
                return host;
            }
        };
    }

    private static ApiClient getClientByHost(final String host) {
        return new ApiClient() {
            @Override
            public String getClientId() {
                return null;
            }

            @Override
            public OkHttpClient getHttpClient() {
                final OkHttpClient client = new OkHttpClient();
                client.setFollowRedirects(false);
                return client;
            }

            @Override
            public HostsProvider getHostsProvider() {
                return getHostsProviderByString(host);
            }

            @Override
            public UserAgent getUserAgent() {
                return null;
            }

            @Override
            public Language getLanguage() {
                return Language.RUSSIAN;
            }
        };
    }
}
