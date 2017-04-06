/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 NBCO Yandex.Money LLC
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

package com.yandex.money.api.typeadapters.methods;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.yandex.money.api.methods.IncomingTransferAccept;
import com.yandex.money.api.model.SimpleStatus;
import com.yandex.money.api.typeadapters.BaseTypeAdapter;
import com.yandex.money.api.typeadapters.model.StatusInfoTypeAdapter;

import java.lang.reflect.Type;

import static com.yandex.money.api.typeadapters.JsonUtils.getInt;
import static com.yandex.money.api.typeadapters.JsonUtils.getString;

/**
 * Type adapter for {@link IncomingTransferAccept}.
 *
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class IncomingTransferAcceptTypeAdapter extends BaseTypeAdapter<IncomingTransferAccept> {

    private static final IncomingTransferAcceptTypeAdapter INSTANCE = new IncomingTransferAcceptTypeAdapter();

    private static final String MEMBER_CODE_ATTEMPTS = "protection_code_attempts_available";
    private static final String MEMBER_EXT_ACTION_URI = "ext_action_uri";

    private IncomingTransferAcceptTypeAdapter() {
    }

    /**
     * @return instance of this class
     */
    public static IncomingTransferAcceptTypeAdapter getInstance() {
        return INSTANCE;
    }

    @Override
    public IncomingTransferAccept deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        JsonObject object = json.getAsJsonObject();
        return new IncomingTransferAccept(StatusInfoTypeAdapter.getInstance().deserialize(json, typeOfT, context),
                getInt(object, MEMBER_CODE_ATTEMPTS), getString(object, MEMBER_EXT_ACTION_URI));
    }

    @Override
    public JsonElement serialize(IncomingTransferAccept src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = StatusInfoTypeAdapter.getInstance()
                .serialize(src.statusInfo, typeOfSrc, context)
                .getAsJsonObject();
        if (src.statusInfo.status == SimpleStatus.REFUSED) {
            switch (src.statusInfo.error) {
                case ILLEGAL_PARAM_PROTECTION_CODE:
                    object.addProperty(MEMBER_CODE_ATTEMPTS, src.protectionCodeAttemptsAvailable);
                    break;
                case EXT_ACTION_REQUIRED:
                    object.addProperty(MEMBER_EXT_ACTION_URI, src.extActionUri);
                    break;
            }
        }
        return object;
    }

    @Override
    public Class<IncomingTransferAccept> getType() {
        return IncomingTransferAccept.class;
    }
}
