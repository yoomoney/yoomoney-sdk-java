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

package com.yandex.money.api.typeadapters.model.showcase;

import com.yandex.money.api.model.showcase.components.Component;
import com.yandex.money.api.model.showcase.components.containers.Expand;
import com.yandex.money.api.model.showcase.components.containers.Group;
import com.yandex.money.api.model.showcase.components.containers.Paragraph;
import com.yandex.money.api.model.showcase.components.uicontrols.AdditionalData;
import com.yandex.money.api.model.showcase.components.uicontrols.Amount;
import com.yandex.money.api.model.showcase.components.uicontrols.Checkbox;
import com.yandex.money.api.model.showcase.components.uicontrols.Date;
import com.yandex.money.api.model.showcase.components.uicontrols.Email;
import com.yandex.money.api.model.showcase.components.uicontrols.Month;
import com.yandex.money.api.model.showcase.components.uicontrols.Number;
import com.yandex.money.api.model.showcase.components.uicontrols.Select;
import com.yandex.money.api.model.showcase.components.uicontrols.Submit;
import com.yandex.money.api.model.showcase.components.uicontrols.Tel;
import com.yandex.money.api.model.showcase.components.uicontrols.Text;
import com.yandex.money.api.model.showcase.components.uicontrols.TextArea;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
public final class ComponentsTypeProvider {

    private static final Map<Type, String> REVERSE_TYPE_MAPPING;
    private static final Map<Component.Type, Type> TYPE_MAPPING;

    static {
        Map<Component.Type, Type> typeMapping = new HashMap<>();

        typeMapping.put(Component.Type.AMOUNT, Amount.class);
        typeMapping.put(Component.Type.CHECKBOX, Checkbox.class);
        typeMapping.put(Component.Type.DATE, Date.class);
        typeMapping.put(Component.Type.EMAIL, Email.class);
        typeMapping.put(Component.Type.GROUP, Group.class);
        typeMapping.put(Component.Type.MONTH, Month.class);
        typeMapping.put(Component.Type.NUMBER, Number.class);
        typeMapping.put(Component.Type.PARAGRAPH, Paragraph.class);
        typeMapping.put(Component.Type.SELECT, Select.class);
        typeMapping.put(Component.Type.SUBMIT, Submit.class);
        typeMapping.put(Component.Type.TEL, Tel.class);
        typeMapping.put(Component.Type.TEXT, Text.class);
        typeMapping.put(Component.Type.TEXT_AREA, TextArea.class);
        typeMapping.put(Component.Type.ADDITIONAL_DATA, AdditionalData.class);
        typeMapping.put(Component.Type.EXPAND, Expand.class);

        TYPE_MAPPING = Collections.unmodifiableMap(typeMapping);

        Map<Type, String> reverseTypeMapping = new HashMap<>();

        for (Map.Entry<Component.Type, Type> item : TYPE_MAPPING.entrySet()) {
            reverseTypeMapping.put(item.getValue(), item.getKey().code);
        }

        REVERSE_TYPE_MAPPING = Collections.unmodifiableMap(reverseTypeMapping);
    }

    private ComponentsTypeProvider() {
    }

    /**
     * @return mapping from {@link Component.Type} to {@link Type}
     */
    public static Type getClassOfComponentType(Component.Type componentType) {
        return TYPE_MAPPING.get(componentType);
    }

    /**
     * @return mapping from {@link Type} to {@link Component.Type}
     */
    public static String getTypeFromClass(Type type) {
        return REVERSE_TYPE_MAPPING.get(type);
    }
}
