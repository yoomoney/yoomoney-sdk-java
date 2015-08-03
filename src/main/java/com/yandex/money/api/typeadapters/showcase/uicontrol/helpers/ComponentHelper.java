package com.yandex.money.api.typeadapters.showcase.uicontrol.helpers;

import com.google.gson.JsonObject;
import com.yandex.money.api.model.showcase.components.Component;

/**
 * @author Anton Ermak (ermak@yamoney.ru)
 */
interface ComponentHelper<T extends Component, U extends Component.Builder> {

    void serialize(T from, JsonObject to);

    void deserialize(JsonObject from, U to);
}
