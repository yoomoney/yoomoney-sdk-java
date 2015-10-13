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

package com.yandex.money.api.model;

/**
 * @author Slava Yasevich (vyasevich@yamoney.ru)
 */
public class YandexMoneyCard extends Card {

    public final State state;

    protected YandexMoneyCard(Builder builder) {
        super(builder);
        this.state = builder.state;
    }

    @Override
    public String toString() {
        return "YandexMoneyCard{" +
                "id='" + id + '\'' +
                ", panFragment='" + panFragment + '\'' +
                ", type=" + type +
                ", state=" + state +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        YandexMoneyCard that = (YandexMoneyCard) o;

        return state == that.state;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + state.hashCode();
        return result;
    }

    public enum State {
        ACTIVE("active"),
        ACTIVE_NO_PIN("active_no_pin"),
        AWAITING_ACTIVATION("awaiting_activation"),
        BLOCKED("blocked"),
        UNKNOWN("unknown");

        public final String code;

        State(String code) {
            this.code = code;
        }

        public static State parse(String code) {
            if (code == null) {
                throw new NullPointerException("code is null");
            }
            for (State value : values()) {
                if (value.code.equals(code)) {
                    return value;
                }
            }
            throw new IllegalArgumentException("unknown code: '" + code + "'");
        }
    }

    public static class Builder extends Card.Builder {

        private State state = State.UNKNOWN;

        public Builder setState(State state) {
            if (state == null) {
                throw new NullPointerException("state is null");
            }
            this.state = state;
            return this;
        }

        @Override
        public YandexMoneyCard create() {
            return new YandexMoneyCard(this);
        }
    }
}
