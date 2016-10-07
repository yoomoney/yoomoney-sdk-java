package com.yandex.money.api.model;

import com.yandex.money.api.util.Enums;

public class VirtualCard extends Card{

    public final State state;

    protected VirtualCard(Builder builder) {
        super(builder);
        this.state = builder.state;
    }

    @Override
    public String toString() {
        return "VirtualCard{" +
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

        VirtualCard that = (VirtualCard) o;

        return state == that.state;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + state.hashCode();
        return result;
    }

    public enum State implements Enums.WithCode<State> {
        ACTIVE("active"),
        ACTIVE_NO_PIN("active_no_pin"),
        UNKNOWN("unknown"),
        EXPIRED("expired"),
        BLOCKED("blocked");

        public final String code;

        State(String code) {
            this.code = code;
        }

        @Override
        public String getCode() {
            return code;
        }

        @Override
        public State[] getValues() {
            return values();
        }

        public static State parse(String code) {
            return Enums.parse(UNKNOWN, UNKNOWN, code);
        }
    }

    public static class Builder extends Card.Builder {

        private State state = State.UNKNOWN;

        public Builder setState(State state) {
            if (state != null) {
                this.state = state;
            }
            return this;
        }

        @Override
        public VirtualCard create() {
            return new VirtualCard(this);
        }
    }
}
