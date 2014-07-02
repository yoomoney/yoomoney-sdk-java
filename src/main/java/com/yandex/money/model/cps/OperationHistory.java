package com.yandex.money.model.cps;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.yandex.money.model.cps.misc.Operation;

import org.joda.time.DateTime;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class OperationHistory {

    private final String error;
    private final String nextRecord;
    private final List<Operation> operations;

    public OperationHistory(String error, String nextRecord, List<Operation> operations) {
        this.error = error;
        this.nextRecord = nextRecord;
        this.operations = operations;
    }

    public String getError() {
        return error;
    }

    public String getNextRecord() {
        return nextRecord;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public static final class Deserializer implements JsonDeserializer<OperationHistory> {
        @Override
        public OperationHistory deserialize(JsonElement json, Type typeOfT,
                                            JsonDeserializationContext context)
                throws JsonParseException {

            JsonObject object = json.getAsJsonObject();

            final String operationsMember = "operations";
            List<Operation> operations = null;
            if (object.has(operationsMember)) {
                operations = new ArrayList<>();
                for (JsonElement element : object.getAsJsonArray(operationsMember)) {
                    operations.add(Operation.createFromJson(element));
                }
            }

            return new OperationHistory(JsonUtils.getString(object, "error"),
                    JsonUtils.getString(object, "next_record"), operations);
        }
    }

    public static class Request {

        private final Set<FilterType> types;
        private final String label;
        private final DateTime from;
        private final DateTime till;
        private final String startRecord;
        private final Integer records;
        private final Boolean details;

        private Request(Set<FilterType> types, String label, DateTime from, DateTime till,
                        String startRecord, Integer records, Boolean details) {

            this.types = types;
            this.label = label;
            if (from != null && till != null && from.isAfter(till)) {
                throw new IllegalArgumentException("\'from\' should be before \'till\'");
            }
            this.from = from;
            this.till = till;
            this.startRecord = startRecord;
            if (records != null) {
                if (records < 1) {
                    records = 1;
                } else if (records > 100) {
                    records = 100;
                }
            }
            this.records = records;
            this.details = details;
        }

        public static class Builder {
            private Set<FilterType> types;
            private String label;
            private DateTime from;
            private DateTime till;
            private String startRecord;
            private Integer records;
            private Boolean details;

            public Builder setTypes(Set<FilterType> types) {
                this.types = types;
                return this;
            }

            public Builder setLabel(String label) {
                this.label = label;
                return this;
            }

            public Builder setFrom(DateTime from) {
                this.from = from;
                return this;
            }

            public Builder setTill(DateTime till) {
                this.till = till;
                return this;
            }

            public Builder setStartRecord(String startRecord) {
                this.startRecord = startRecord;
                return this;
            }

            public Builder setRecords(Integer records) {
                this.records = records;
                return this;
            }

            public Builder setDetails(Boolean details) {
                this.details = details;
                return this;
            }

            public Request createRequest() {
                return new Request(types, label, from, till, startRecord, records, details);
            }
        }
    }

    public enum FilterType {

        DEPOSITION("deposition"),
        PAYMENT("payment"),
        INCOMING_TRANSFER_UNACCEPTED("incoming-transfers-unaccepted");

        private final String filterType;

        private FilterType(String filterType) {
            this.filterType = filterType;
        }

        @Override
        public String toString() {
            return filterType;
        }
    }
}
